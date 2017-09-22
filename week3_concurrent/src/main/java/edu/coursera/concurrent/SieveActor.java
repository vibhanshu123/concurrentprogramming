package edu.coursera.concurrent;

import java.util.ArrayList;
import java.util.List;

import edu.rice.pcdp.Actor;
import edu.rice.pcdp.PCDP;




/**
 * An actor-based implementation of the Sieve of Eratosthenes.
 *
 * TODO Fill in the empty SieveActorActor actor class below and use it from
 * countPrimes to determin the number of primes <= limit.
 */
public final class SieveActor extends Sieve {
	
	
	
    /**
     * {@inheritDoc}
     *
     * TODO Use the SieveActorActor class to calculate the number of primes <=
     * limit in parallel. You might consider how you can model the Sieve of
     * Eratosthenes as a pipeline of actors, each corresponding to a single
     * prime number.
     */
    @Override
    public int countPrimes(final int limit) {
    	//final List<Integer> localPrimes = new ArrayList<Integer>();
    	
    		 final SieveActorActor sieveActor= new SieveActorActor(2);
    		 PCDP.finish(()->{
    	        for (int i = 3; i <= limit; i += 2) {
    	            sieveActor.send(i);
    	        }

    	        
    	 });
    	 return sieveActor.getLocalPrimes();
    }

    /**
     * An actor class that helps implement the Sieve of Eratosthenes in
     * parallel.
     */
    public final class SieveActorActor extends Actor {
    	
    	private final List<Integer> localPrimes = new ArrayList<Integer>();
    	private SieveActorActor nextActor;
    	
    	
        public SieveActorActor(int localPrime) {
			super();
			this.localPrimes.add(localPrime);
			this.nextActor = null;
		}



		public SieveActorActor getNextActor() {
			return nextActor;
		}
		
		
		


		public Integer getLocalPrimes() {
			return localPrimes.size();
		}



		/**
         * Process a single message sent to this actor.
         *
         * TODO complete this method.
         *
         * @param msg Received message
         */
	    @Override
        public void process(Object msg) {
           final int candidate = (Integer) msg;
           if(candidate<=0) {
        	   if(nextActor!=null) {
        		   nextActor.send(msg);
        	   }
        	   
           }else {
        	    boolean locallyPrime = isLocallyPrime(candidate);
        	   if(locallyPrime) {
        			   localPrimes.add(candidate);
        			   //numLocalPrimes+=1;
        		   }else if(nextActor==null) {
        			   nextActor= new SieveActorActor(candidate);
        			  // nextActor.start();
        		   }else {
        			   nextActor.send(msg);
        		   }
        	   }
           }
	    
	    private boolean isLocallyPrime(int candidate) {
			// TODO Auto-generated method stub
			final boolean[] isPrime = {true};
			checkPrimeKernel(candidate,isPrime);
			return isPrime[0];
		}


		private void checkPrimeKernel(int candidate, boolean[] isPrime) {
			for(int i=0;i<localPrimes.size();i++) {
				if(candidate%localPrimes.get(i)==0) {
					isPrime[0]=false;
				}
			}
			
		}
	    
	    
        }


		


    
}

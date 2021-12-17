package com.tokopedia.sellerapp.anr;

/**
 * A {@link Runnable} which calls {@link #notifyAll()} when run.
 */
class AnrSupervisorCallback implements Runnable {

    /**
     * Flag storing whether {@link #run()} was called
     */
    private boolean mCalled;

    /**
     * Creates a new instance
     */
    public AnrSupervisorCallback() {
        super();

    }

    @Override
    public synchronized void run() {
        this.mCalled = true;
        this.notifyAll();

    }

    /**
     * Returns whether {@link #run()} was called yet
     *
     * @return true if called, false if not
     */
    synchronized boolean isCalled() {
        return this.mCalled;

    }
}

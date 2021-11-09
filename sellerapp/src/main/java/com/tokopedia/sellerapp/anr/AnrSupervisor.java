package com.tokopedia.sellerapp.anr;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * A class supervising the UI thread for ANR errors. Use
 * {@link #start()} and {@link #stop()} to control
 * when the UI thread is supervised
 */
public class AnrSupervisor {
    /**
     * The {@link ExecutorService} checking the UI thread
     */
    private ExecutorService mExecutor =
            Executors.newSingleThreadExecutor();
    /**
     * The {@link AnrSupervisorRunnable} running on a separate
     * thread
     */
    private final AnrSupervisorRunnable mSupervisor =
            new AnrSupervisorRunnable();

    /**
     * Starts the supervision
     */
    public synchronized void start() {
        synchronized (this.mSupervisor) {
            if (this.mSupervisor.isStopped()) {
                this.mExecutor.execute(this.mSupervisor);

            } else {
                this.mSupervisor.unstopp();

            }
        }
    }

    /**
     * Stops the supervision. The stop is delayed, so if
     * start() is called right after stop(),
     * both methods will have no effect. There will be at least one
     * more ANR check before the supervision is stopped.
     */
    public synchronized void stop() {
        this.mSupervisor.stop();

    }
}
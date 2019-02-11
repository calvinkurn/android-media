package com.tokopedia.websocket;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Func1;


/**
 * Created by Steven.
 */

public final class RetryObservable implements Func1<Observable<? extends Throwable>, Observable<?>> {
    private int retryCount;
    private int maxRetries;
    private long delay;
    private String logTag = "MainActivity RxWebSocket";


    RetryObservable(int maxRetries, long delayInSecond) {
        this.retryCount = 0;
        this.maxRetries = maxRetries;
        this.delay = delayInSecond;
    }

    @Override
    public Observable<?> call(Observable<? extends Throwable> attempts) {
        return attempts.flatMap((Func1<Throwable, Observable<?>>) throwable -> {
            if (++retryCount <= maxRetries) {
                Log.d(logTag, "retry " + retryCount + " " + delay * retryCount);
                return Observable.timer(delay * retryCount, TimeUnit.MILLISECONDS);
            } else {
                return Observable.error(throwable);
            }
        });
    }

    public void resetMaxRetries() {
        this.maxRetries = 0;
    }
}

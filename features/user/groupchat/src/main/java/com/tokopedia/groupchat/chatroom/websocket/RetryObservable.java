package com.tokopedia.groupchat.chatroom.websocket;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Func1;


/**
 * Created by Steven.
 */

public final class RetryObservable implements Func1<Observable<? extends Throwable>, Observable<?>> {
    private int retryCount;
    private final int maxRetries;
    private long delay;
    private String logTag = "MainActivity RxWebSocket";


    RetryObservable(int maxRetries, long delay) {
        this.retryCount = 0;
        this.maxRetries = maxRetries;
        this.delay = delay;
    }

    @Override
    public Observable<?> call(Observable<? extends Throwable> attempts) {
        return attempts.flatMap((Func1<Throwable, Observable<?>>) throwable -> {
            if (++retryCount < maxRetries) {
                delay = delay ^ retryCount;
                Log.d(logTag, "retry " + retryCount + " " + delay);
                return Observable.timer(delay, TimeUnit.SECONDS);
            } else {
                return Observable.error(throwable);
            }
        });
    }
}

package com.tokopedia.groupchat.chatroom.websocket;

/**
 * @author : Steven 06/10/18
 */

import android.util.Log;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Func1;

public final class RetryObservable implements Func1<Observable<? extends Throwable>, Observable<?>> {
    private int retryCount;
    private final int maxRetries;
    private long delay;
    private String logTag = "MainActivity RxWebSocket";


    public RetryObservable(int maxRetries, long delay) {
        this.retryCount = 0;
        this.maxRetries = maxRetries;
        this.delay = delay;
    }

    @Override
    public Observable<?> call(Observable<? extends Throwable> attempts) {
        return attempts.flatMap(new Func1<Throwable, Observable<?>>() {
            @Override
            public Observable<?> call(Throwable throwable) {
                if (++retryCount < maxRetries) {
                    if(retryCount > 1){
                        delay = delay* retryCount / (retryCount -1);
                    }
                    Log.d(logTag, "retry " +retryCount + " "+ delay);
                    return Observable.timer(delay, TimeUnit.SECONDS);
                } else {
                    return Observable.error(throwable);
                }
            }
        });
    }
}

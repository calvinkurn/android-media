package com.tokopedia.sellerapp;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.plugins.RxJavaObservableExecutionHook;

/**
 * Created by pt2121 on 3/7/15.
 */
public class RxJavaExecutionHook extends RxJavaObservableExecutionHook {

    RxJavaExecutionBridge rxJavaExecutionBridge;

    public RxJavaExecutionHook(RxJavaExecutionBridge rxJavaExecutionBridge) {
        this.rxJavaExecutionBridge = rxJavaExecutionBridge;
    }

    @Override
    public <T> Observable.OnSubscribe<T> onCreate(Observable.OnSubscribe<T> f) {
        return super.onCreate(f);
    }

    @Override
    public <T> Observable.OnSubscribe<T> onSubscribeStart(Observable<? extends T> observableInstance,
                                                          Observable.OnSubscribe<T> onSubscribe) {

        onSubscribe.call(new Subscriber<T>() {
            @Override
            public void onCompleted() {
                rxJavaExecutionBridge.onEnd();
            }

            @Override
            public void onError(Throwable e) {
                rxJavaExecutionBridge.onEnd();
            }

            @Override
            public void onNext(T t) {

            }
        });
        rxJavaExecutionBridge.onStart();
        return onSubscribe;

    }

    @Override
    public <T> Subscription onSubscribeReturn(Subscription subscription) {
        return subscription;
    }

    @Override
    public <T> Throwable onSubscribeError(Throwable e) {
        return e;
    }

    @Override
    public <T, R> Observable.Operator<? extends R, ? super T> onLift(Observable.Operator<? extends R, ? super T> lift) {
        return super.onLift(lift);
    }
}

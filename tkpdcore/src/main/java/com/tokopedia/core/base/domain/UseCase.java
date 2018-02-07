package com.tokopedia.core.base.domain;

import android.util.Log;

import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import rx.subscriptions.Subscriptions;

/**
 * @author Kulomady on 2/1/17.
 */

/**
 * Use Usecase from tkpd usecase
 */
@Deprecated
public abstract class UseCase<T> implements Interactor<T> {

    private CompositeSubscription compositeSubscription = new CompositeSubscription();

    protected ThreadExecutor threadExecutor;
    protected PostExecutionThread postExecutionThread;
    protected Subscription subscription = Subscriptions.empty();

    public UseCase(ThreadExecutor threadExecutor,
                   PostExecutionThread postExecutionThread) {
        this.threadExecutor = threadExecutor;
        this.postExecutionThread = postExecutionThread;
    }

    public UseCase() {
        this(null, null);
    }

    public abstract Observable<T> createObservable(RequestParams requestParams);

    public final T getData(RequestParams requestParams) {
        return createObservable(requestParams).defaultIfEmpty(null).toBlocking().first();
    }

    public final void execute(RequestParams requestParams, Subscriber<T> subscriber) {
        execute(requestParams, subscriber, false);
    }

    public final void executeSync(RequestParams requestParams) {
        execute(requestParams, null, true);
    }

    public final void executeSync(RequestParams requestParams, Subscriber<T> subscriber) {
        execute(requestParams, subscriber, true);
    }

    private void execute(RequestParams requestParams, Subscriber<T> subscriber, boolean sync) {
        try {
            if (compositeSubscription == null || compositeSubscription.isUnsubscribed()) {
                compositeSubscription = new CompositeSubscription();
            }
            Observable<T> observable;
            if (sync) {
                observable = Observable.just(createObservable(requestParams)
                        .defaultIfEmpty(null).toBlocking().first())
                        .subscribeOn(Schedulers.from(threadExecutor))
                        .observeOn(postExecutionThread.getScheduler());
            } else {
                observable = createObservable(requestParams)
                        .subscribeOn(Schedulers.from(threadExecutor))
                        .observeOn(postExecutionThread.getScheduler());
            }
            if (subscriber != null) {
                subscription = observable.subscribe(subscriber);
                compositeSubscription.add(subscription);
            }
        } catch (Throwable t) {
            Log.d("Pranay","UseCase Catch");
            t.printStackTrace();
        }
    }

    public void unsubscribe() {
        if (!compositeSubscription.isUnsubscribed()) {
            compositeSubscription.unsubscribe();
        }
    }

    @Override
    public Observable<T> getExecuteObservable(RequestParams requestParams) {
        return createObservable(requestParams);
    }

    public Observable<T> getExecuteObservableAsync(RequestParams requestParams){
        return createObservable(requestParams)
                .subscribeOn(Schedulers.from(this.threadExecutor))
                .observeOn(this.postExecutionThread.getScheduler());
    }
}

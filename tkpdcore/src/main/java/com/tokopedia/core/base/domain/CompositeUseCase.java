package com.tokopedia.core.base.domain;

import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @author hendry
 */

public abstract class CompositeUseCase<T> extends UseCase<T> {
    private CompositeSubscription compositeSubscription = new CompositeSubscription();

    public CompositeUseCase(ThreadExecutor threadExecutor,
                            PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
    }

    public abstract Observable<T> createObservable(RequestParams requestParams);

    @Override
    public void execute(RequestParams requestParams, Subscriber<T> subscriber) {
        if (compositeSubscription == null || compositeSubscription.isUnsubscribed()) {
            compositeSubscription =  new CompositeSubscription();
        }
        this.subscription = createObservable(requestParams)
                .subscribeOn(Schedulers.from(threadExecutor))
                .observeOn(postExecutionThread.getScheduler())
                .subscribe(subscriber);
        compositeSubscription.add(subscription);
    }

    @Override
    public void unsubscribe() {
        if (!this.compositeSubscription.isUnsubscribed()) {
            this.compositeSubscription.unsubscribe();
        }
    }

    @Override
    public Observable<T> getExecuteObservable(RequestParams requestParams) {
        return createObservable(requestParams);
    }
}

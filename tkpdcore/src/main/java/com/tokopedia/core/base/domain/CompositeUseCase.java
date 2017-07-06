package com.tokopedia.core.base.domain;

import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import rx.subscriptions.Subscriptions;

/**
 * @author hendry
 */

public abstract class CompositeUseCase<T> implements Interactor<T> {
    private ThreadExecutor threadExecutor;
    private PostExecutionThread postExecutionThread;
    protected Subscription subscription = Subscriptions.empty();
    private CompositeSubscription compositeSubscription = new CompositeSubscription();

    public CompositeUseCase(ThreadExecutor threadExecutor,
                            PostExecutionThread postExecutionThread) {
        this.threadExecutor = threadExecutor;
        this.postExecutionThread = postExecutionThread;
    }

    public abstract Observable<T> createObservable(RequestParams requestParams);

    public void execute(RequestParams requestParams, Subscriber<T> subscriber) {
        this.subscription = createObservable(requestParams)
                .subscribeOn(Schedulers.from(threadExecutor))
                .observeOn(postExecutionThread.getScheduler())
                .subscribe(subscriber);
        compositeSubscription.add(subscription);
    }

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

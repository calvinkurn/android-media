package com.tokopedia.core.base;

import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

/**
 * @author Kulomady on 12/9/16.
 */

public abstract class UseCaseWithParams
        <P extends DefaultParams, T> implements InteractorParams<P, T> {

    private final ThreadExecutor threadExecutor;
    private final PostExecutionThread postExecutionThread;
    private Subscription subscription = Subscriptions.empty();

    protected UseCaseWithParams(ThreadExecutor threadExecutor,
                                PostExecutionThread postExecutionThread) {

        this.threadExecutor = threadExecutor;
        this.postExecutionThread = postExecutionThread;
    }

    protected abstract Observable<T> createObservable(P requestParams);

    @Override
    public void execute(P requestParams, Subscriber<T> subscriber) {
        this.subscription = createObservable(requestParams)
                .subscribeOn(Schedulers.from(threadExecutor))
                .observeOn(postExecutionThread.getScheduler())
                .subscribe(subscriber);
    }

    @Override
    public Observable<T> execute(P requestValues) {
        return createObservable(requestValues);
    }

    public void unsubscribe() {
        if (subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
}

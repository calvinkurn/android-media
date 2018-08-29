package com.tokopedia.posapp.base.domain;

import com.tokopedia.core.base.domain.DefaultParams;
import com.tokopedia.core.base.domain.InteractorParams;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

/**
 * Created by okasurya on 8/22/17.
 */

public abstract class UseCaseWithParams<P extends DefaultParams, T> implements InteractorParams<P,T> {
    private ThreadExecutor threadExecutor;
    private PostExecutionThread postExecutionThread;
    protected Subscription subscription = Subscriptions.empty();

    public UseCaseWithParams(ThreadExecutor threadExecutor,
                             PostExecutionThread postExecutionThread) {
        this.threadExecutor = threadExecutor;
        this.postExecutionThread = postExecutionThread;
    }

    public abstract Observable<T> createObservable(P requestParams);

    @Override
    public void execute(P requestParams, Subscriber<T> subscriber) {
        this.subscription = createObservable(requestParams)
                .subscribeOn(Schedulers.from(threadExecutor))
                .observeOn(postExecutionThread.getScheduler())
                .subscribe(subscriber);
    }

    @Override
    public Observable<T> execute(P requestParams) {
        return createObservable(requestParams);
    }

    public void unsubscribe() {
        if (!this.subscription.isUnsubscribed()) {
            this.subscription.unsubscribe();
        }
    }
}

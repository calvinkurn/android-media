package com.tokopedia.core.base;

import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

/**
 * @author Kulomady on 12/7/16.
 */

public abstract class UseCase<T> implements Interactor<T> {

    private ThreadExecutor threadExecutor;
    private PostExecutionThread postExecutionThread;
    private Subscription subscription = Subscriptions.empty();

    public UseCase(ThreadExecutor threadExecutor,
                   PostExecutionThread postExecutionThread) {
        this.threadExecutor = threadExecutor;
        this.postExecutionThread = postExecutionThread;
    }

    protected abstract Observable<T> createObservable();

    @Override
    public void execute(Subscriber<T> subscriber) {
        this.subscription = createObservable()
                .subscribeOn(Schedulers.from(threadExecutor))
                .observeOn(postExecutionThread.getScheduler())
                .subscribe(subscriber);
    }

    @Override
    public Observable<T> execute() {
        return createObservable();
    }

    public void unsubcribe() {
        if (subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
}

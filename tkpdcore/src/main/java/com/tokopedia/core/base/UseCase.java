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

    private ThreadExecutor mThreadExecutor;
    private PostExecutionThread mPostExecutionThread;
    private Subscription mSubscription = Subscriptions.empty();

    public UseCase(ThreadExecutor threadExecutor,
                   PostExecutionThread postExecutionThread) {
        mThreadExecutor = threadExecutor;
        mPostExecutionThread = postExecutionThread;
    }

    protected abstract Observable<T> createObservable();

    @Override
    public void execute(Subscriber<T> subscriber) {
        this.mSubscription = createObservable()
                .subscribeOn(Schedulers.from(mThreadExecutor))
                .observeOn(mPostExecutionThread.getScheduler())
                .subscribe(subscriber);
    }

    @Override
    public Observable<T> execute() {
        return createObservable();
    }

    public void unsubcribe() {
        if (mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }
}

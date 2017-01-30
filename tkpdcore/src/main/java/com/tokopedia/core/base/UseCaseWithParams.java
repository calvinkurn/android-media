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

    private final ThreadExecutor mThreadExecutor;
    private final PostExecutionThread mPostExecutionThread;
    private Subscription mSubscription = Subscriptions.empty();

    protected UseCaseWithParams(ThreadExecutor threadExecutor,
                                PostExecutionThread postExecutionThread) {

        mThreadExecutor = threadExecutor;
        mPostExecutionThread = postExecutionThread;
    }

    protected abstract Observable<T> createObservable(P requestParams);

    @Override
    public void execute(P requestParams, Subscriber<T> subscriber) {
        this.mSubscription = createObservable(requestParams)
                .subscribeOn(Schedulers.from(mThreadExecutor))
                .observeOn(mPostExecutionThread.getScheduler())
                .subscribe(subscriber);
    }

    @Override
    public Observable<T> execute(P requestValues) {
        return createObservable(requestValues);
    }

    public void unsubscribe() {
        if (mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }
}

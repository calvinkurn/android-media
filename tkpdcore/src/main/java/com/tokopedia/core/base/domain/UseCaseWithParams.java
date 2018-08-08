package com.tokopedia.core.base.domain;

import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

/**
 * @author erry on 23/02/17.
 */

/**
 * Extends usecase from tkpd usecase and move to specific module for specific need (Only used in tkpd discovery)
 * @param <P>
 * @param <T>
 */
@Deprecated
public abstract class UseCaseWithParams
        <P extends DefaultParams, T> implements InteractorParams<P, T> {

    private Subscription mSubscription = Subscriptions.empty();

    protected UseCaseWithParams() {
    }

    protected abstract Observable<T> createObservable(P requestParams);

    @Override
    public void execute(P requestParams, Subscriber<T> subscriber) {
        this.mSubscription = createObservable(requestParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
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
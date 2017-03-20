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
 * @author Kulomady on 2/1/17.
 */

public abstract class DefaultUseCase<T> implements Interactor<T> {
    private Subscription subscription = Subscriptions.empty();

    public DefaultUseCase() {
    }

    public abstract Observable<T> createObservable(RequestParams requestParams);

    public void execute(RequestParams requestParams, Subscriber<T> subscriber) {
        this.subscription = createObservable(requestParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(subscriber);
    }

    public void unsubscribe() {
        if (!this.subscription.isUnsubscribed()) {
            this.subscription.unsubscribe();
        }
    }

    @Override
    public Observable<T> getExecuteObservable(RequestParams requestParams) {
        return createObservable(requestParams);
    }
}

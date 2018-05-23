package com.tokopedia.commongraphql;

import com.tokopedia.usecase.Interactor;
import com.tokopedia.usecase.RequestParams;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import rx.subscriptions.Subscriptions;

/**
 * @author ricoharisin .
 */

public abstract class BaseGraphqlUseCase<T> implements Interactor<T> {

    private static final String QUERY_KEY = "query";
    private BaseGraphqlRepository baseGraphqlRepository;

    protected Subscription subscription = Subscriptions.empty();
    private CompositeSubscription compositeSubscription = new CompositeSubscription();

    public BaseGraphqlUseCase(BaseGraphqlRepository baseGraphqlRepository) {
        this.baseGraphqlRepository = baseGraphqlRepository;
    }

    public abstract Observable<T> createObservable(RequestParams requestParams);

    public abstract String getQuery();

    public abstract Func1<String, T> getMapper();

    @Override
    public void execute(RequestParams requestParams, Subscriber<T> subscriber) {
        if (getQuery() == null) throw new RuntimeException("query can't be empty!");
        requestParams.putString(QUERY_KEY, getQuery());
        try {
            if (compositeSubscription == null || compositeSubscription.isUnsubscribed()) {
                compositeSubscription = new CompositeSubscription();
            }
            Observable<T> observable = createObservable(requestParams)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
            if (subscriber != null) {
                subscription = observable.subscribe(subscriber);
                compositeSubscription.add(subscription);
            }
        } catch (Throwable t) {
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
}

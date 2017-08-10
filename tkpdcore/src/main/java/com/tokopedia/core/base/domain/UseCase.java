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
 * @author Kulomady on 2/1/17.
 */

public abstract class UseCase<T> implements Interactor<T> {

    private CompositeSubscription compositeSubscription = new CompositeSubscription();

    protected ThreadExecutor threadExecutor;
    protected PostExecutionThread postExecutionThread;
    protected Subscription subscription = Subscriptions.empty();

    public UseCase(ThreadExecutor threadExecutor,
                   PostExecutionThread postExecutionThread) {
        this.threadExecutor = threadExecutor;
        this.postExecutionThread = postExecutionThread;
    }

    public UseCase(){
        this(null,null);
    }

    public abstract Observable<T> createObservable(RequestParams requestParams);

    public final Observable<T> createObservableSync(RequestParams requestParams) {
        return Observable.just(createObservable(requestParams).toBlocking().first());
    }

    public final void execute(RequestParams requestParams, Subscriber<T> subscriber) {
        subscription = createObservable(requestParams)
                .subscribeOn(Schedulers.from(threadExecutor))
                .observeOn(postExecutionThread.getScheduler())
                .subscribe(subscriber);
        compositeSubscription.add(subscription);
    }

    public final void executeSync(RequestParams requestParams, Subscriber<T> subscriber) {
        subscription = createObservableSync(requestParams)
                .subscribeOn(Schedulers.from(threadExecutor))
                .observeOn(postExecutionThread.getScheduler())
                .subscribe(subscriber);
        compositeSubscription.add(subscription);
    }

    public final Observable<T> execute(RequestParams requestParams){
        return createObservableSync(requestParams);
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

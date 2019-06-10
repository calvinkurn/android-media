package com.tokopedia.search.result.domain.usecase;

import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;
import rx.Subscriber;

public class TestUseCase<T> extends UseCase<T> {

    private T data;

    public TestUseCase(T data) {
        this.data = data;
    }

    @Override
    public Observable<T> createObservable(RequestParams requestParams) {
        return Observable.just(data);
    }

    @Override
    public void execute(RequestParams requestParams, Subscriber<T> subscriber) {
        createObservable(requestParams).subscribe(subscriber);
    }
}

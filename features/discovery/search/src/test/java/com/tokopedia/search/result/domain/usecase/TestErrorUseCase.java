package com.tokopedia.search.result.domain.usecase;

import com.tokopedia.usecase.RequestParams;

import rx.Observable;

public class TestErrorUseCase<T, E extends Exception> extends TestUseCase<T> {

    public E error;

    public TestErrorUseCase(E error) {
        super(null);

        this.error = error;
    }

    @Override
    public Observable<T> createObservable(RequestParams requestParams) {
        return Observable.error(error);
    }
}

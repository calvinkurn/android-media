package com.tokopedia.search.result.domain.usecase;

import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Subscriber;

public abstract class TestUseCase<T> extends UseCase<T> {

    @Override
    public void execute(RequestParams requestParams, Subscriber<T> subscriber) {
        createObservable(requestParams).subscribe(subscriber);
    }
}

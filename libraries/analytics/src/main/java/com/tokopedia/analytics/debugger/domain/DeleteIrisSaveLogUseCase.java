package com.tokopedia.analytics.debugger.domain;

import com.tokopedia.analytics.debugger.data.repository.IrisSaveLogLocalRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

public class DeleteIrisSaveLogUseCase extends UseCase<Boolean> {
    private IrisSaveLogLocalRepository repository;

    @Inject
    DeleteIrisSaveLogUseCase(IrisSaveLogLocalRepository repository) {
        this.repository = repository;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return repository.removeAll();
    }
}

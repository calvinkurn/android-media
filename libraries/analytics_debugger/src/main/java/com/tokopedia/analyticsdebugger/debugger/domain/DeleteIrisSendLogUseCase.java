package com.tokopedia.analyticsdebugger.debugger.domain;

import com.tokopedia.analyticsdebugger.debugger.data.repository.IrisSendLogLocalRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

public class DeleteIrisSendLogUseCase extends UseCase<Boolean> {
    private IrisSendLogLocalRepository repository;

    @Inject
    DeleteIrisSendLogUseCase(IrisSendLogLocalRepository repository) {
        this.repository = repository;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return repository.removeAll();
    }
}

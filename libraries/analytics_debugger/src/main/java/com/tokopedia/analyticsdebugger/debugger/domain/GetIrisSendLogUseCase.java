package com.tokopedia.analyticsdebugger.debugger.domain;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.analyticsdebugger.debugger.data.repository.IrisSendLogLocalRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

public class GetIrisSendLogUseCase extends UseCase<List<Visitable>> {
    private IrisSendLogLocalRepository repository;

    @Inject
    GetIrisSendLogUseCase(IrisSendLogLocalRepository repository) {
        this.repository = repository;
    }

    @Override
    public Observable<List<Visitable>> createObservable(RequestParams requestParams) {
        return repository.get(requestParams);
    }
}

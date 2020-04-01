package com.tokopedia.analyticsdebugger.debugger.domain;

import com.tokopedia.analyticsdebugger.debugger.data.repository.GtmErrorLogLocalRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

public class DeleteGtmErrorLogUseCase extends UseCase<Boolean> {
    private GtmErrorLogLocalRepository gtmErrorLogLocalRepository;

    @Inject
    DeleteGtmErrorLogUseCase(GtmErrorLogLocalRepository gtmErrorLogLocalRepository) {
        this.gtmErrorLogLocalRepository = gtmErrorLogLocalRepository;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return gtmErrorLogLocalRepository.removeAll();
    }
}

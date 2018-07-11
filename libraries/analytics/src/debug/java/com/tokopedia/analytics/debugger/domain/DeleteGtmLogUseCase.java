package com.tokopedia.analytics.debugger.domain;

import com.tokopedia.analytics.debugger.data.repository.GtmLogLocalRepository;
import com.tokopedia.analytics.debugger.data.repository.GtmLogRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author okasurya on 5/17/18.
 */
public class DeleteGtmLogUseCase extends UseCase<Boolean> {
    private GtmLogRepository gtmLogRepository;

    @Inject
    DeleteGtmLogUseCase(GtmLogLocalRepository gtmLogRepositoory) {
        this.gtmLogRepository = gtmLogRepositoory;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return gtmLogRepository.removeAll();
    }
}

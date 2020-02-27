package com.tokopedia.analytics.debugger.domain;

import com.tokopedia.analytics.debugger.data.repository.FpmLogLocalRepository;
import com.tokopedia.analytics.debugger.data.repository.FpmLogRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author okasurya on 5/17/18.
 */
public class DeleteFpmLogUseCase extends UseCase<Boolean> {
    private FpmLogRepository fpmLogRepository;

    @Inject
    DeleteFpmLogUseCase(FpmLogLocalRepository fpmLogRepositoory) {
        this.fpmLogRepository = fpmLogRepositoory;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return fpmLogRepository.removeAll();
    }
}

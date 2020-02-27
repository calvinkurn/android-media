package com.tokopedia.analytics.debugger.domain;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.analytics.debugger.data.repository.FpmLogLocalRepository;
import com.tokopedia.analytics.debugger.data.repository.FpmLogRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author okasurya on 5/16/18.
 */
public class GetFpmLogUseCase extends UseCase<List<Visitable>> {
    private FpmLogRepository fpmLogRepository;

    @Inject
    GetFpmLogUseCase(FpmLogLocalRepository fpmLogRepository) {
        this.fpmLogRepository = fpmLogRepository;
    }

    @Override
    public Observable<List<Visitable>> createObservable(RequestParams requestParams) {
        return fpmLogRepository.get(requestParams);
    }
}

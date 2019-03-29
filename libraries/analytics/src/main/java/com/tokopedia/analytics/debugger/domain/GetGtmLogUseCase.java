package com.tokopedia.analytics.debugger.domain;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.analytics.debugger.data.repository.GtmLogLocalRepository;
import com.tokopedia.analytics.debugger.data.repository.GtmLogRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author okasurya on 5/16/18.
 */
public class GetGtmLogUseCase extends UseCase<List<Visitable>> {
    private GtmLogRepository gtmLogRepository;

    @Inject
    GetGtmLogUseCase(GtmLogLocalRepository gtmLogRepository) {
        this.gtmLogRepository = gtmLogRepository;
    }

    @Override
    public Observable<List<Visitable>> createObservable(RequestParams requestParams) {
        return gtmLogRepository.get(requestParams);
    }
}

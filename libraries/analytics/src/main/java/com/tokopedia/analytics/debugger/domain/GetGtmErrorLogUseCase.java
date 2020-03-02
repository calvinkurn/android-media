package com.tokopedia.analytics.debugger.domain;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.analytics.debugger.data.repository.GtmErrorLogLocalRepository;
import com.tokopedia.analytics.debugger.data.repository.GtmLogLocalRepository;
import com.tokopedia.analytics.debugger.data.repository.GtmLogRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

public class GetGtmErrorLogUseCase extends UseCase<List<Visitable>> {
    private GtmErrorLogLocalRepository gtmErrorLogLocalRepository;

    @Inject
    GetGtmErrorLogUseCase(GtmErrorLogLocalRepository gtmErrorLogLocalRepository) {
        this.gtmErrorLogLocalRepository = gtmErrorLogLocalRepository;
    }

    @Override
    public Observable<List<Visitable>> createObservable(RequestParams requestParams) {
        return gtmErrorLogLocalRepository.get(requestParams);
    }
}

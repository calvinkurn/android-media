package com.tokopedia.topads.dashboard.domain.interactor;

import com.tokopedia.topads.dashboard.data.model.DataCredit;
import com.tokopedia.topads.dashboard.domain.repository.TopAdsDashboardRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

public class TopAdsGetDataCreditUseCase extends UseCase<List<DataCredit>> {

    private final TopAdsDashboardRepository repository;

    @Inject
    public TopAdsGetDataCreditUseCase(TopAdsDashboardRepository repository) {
        this.repository = repository;
    }

    @Override
    public Observable<List<DataCredit>> createObservable(RequestParams requestParams) {
        return repository.getDashboardCredit(requestParams);
    }
}

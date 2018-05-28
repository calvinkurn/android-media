package com.tokopedia.digital_deals.domain;

import com.tokopedia.digital_deals.domain.model.DealsDomain;
import com.tokopedia.digital_deals.view.presenter.DealsHomePresenter;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

public class GetNextDealPageUseCase extends UseCase<DealsDomain> {
    private final DealsRepository dealsRepository;

    public GetNextDealPageUseCase(DealsRepository eventRepository) {
        super();
        this.dealsRepository = eventRepository;
    }

    @Override
    public Observable<DealsDomain> createObservable(RequestParams requestParams) {
        String nextUrl=requestParams.getString(DealsHomePresenter.TAG, "");
        return dealsRepository.getDeals(nextUrl);
    }

}

package com.tokopedia.digital_deals.domain;

import com.tokopedia.digital_deals.domain.model.DealsDomain;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import rx.Observable;


public class GetDealsListRequestUseCase extends UseCase<DealsDomain> {
    private final DealsRepository dealsRepository;

    public GetDealsListRequestUseCase(DealsRepository eventRepository) {
        super();
        this.dealsRepository = eventRepository;
    }

    @Override
    public Observable<DealsDomain> createObservable(RequestParams requestParams) {
        return dealsRepository.getDeals(requestParams.getParameters());
    }
}

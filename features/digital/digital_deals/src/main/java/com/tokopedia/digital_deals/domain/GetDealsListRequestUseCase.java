package com.tokopedia.digital_deals.domain;

import android.util.Log;

import com.tokopedia.digital_deals.domain.model.DealsCategoryDomain;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import rx.Observable;


public class GetDealsListRequestUseCase extends UseCase<List<DealsCategoryDomain>> {
    private final DealsRepository dealsRepository;

    public GetDealsListRequestUseCase(DealsRepository eventRepository) {
        super();
        this.dealsRepository = eventRepository;
    }

    @Override
    public Observable<List<DealsCategoryDomain>> createObservable(RequestParams requestParams) {
        return dealsRepository.getDeals(requestParams.getParameters());
    }
}

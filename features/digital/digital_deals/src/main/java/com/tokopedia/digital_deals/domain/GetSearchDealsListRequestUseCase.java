package com.tokopedia.digital_deals.domain;

import com.tokopedia.digital_deals.domain.model.searchdomainmodel.SearchDomainModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;



public class GetSearchDealsListRequestUseCase extends UseCase<SearchDomainModel> {
    private final DealsRepository dealsRepository;
    public final String TAG = "tags";

    public GetSearchDealsListRequestUseCase(DealsRepository dealsRepository) {
        super();
        this.dealsRepository = dealsRepository;
    }

    @Override
    public Observable<SearchDomainModel> createObservable(RequestParams requestParams) {
        return dealsRepository.getSearchDeals(requestParams.getParameters());

    }
}

package com.tokopedia.digital_deals.domain;



import com.tokopedia.digital_deals.domain.model.searchdomainmodel.SearchDomainModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

public class GetSearchNextUseCase extends UseCase<SearchDomainModel> {

    private final DealsRepository dealsRepository;

    public GetSearchNextUseCase(DealsRepository dealsRepository) {
        super();
        this.dealsRepository = dealsRepository;
    }

    @Override
    public Observable<SearchDomainModel> createObservable(RequestParams requestParams) {
        String nextUrl = requestParams.getString("nexturl", "");
        return dealsRepository.getSearchNext(nextUrl);
    }
}

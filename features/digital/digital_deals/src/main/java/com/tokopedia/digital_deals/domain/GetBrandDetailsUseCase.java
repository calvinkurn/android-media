package com.tokopedia.digital_deals.domain;

import com.tokopedia.digital_deals.domain.model.branddetailsmodel.BrandDetailsDomain;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

public class GetBrandDetailsUseCase extends UseCase<BrandDetailsDomain> {
    private final DealsRepository dealsRepository;

    public GetBrandDetailsUseCase(DealsRepository dealsRepository){
        super();
        this.dealsRepository=dealsRepository;
    }

    @Override
    public Observable<BrandDetailsDomain> createObservable(RequestParams requestParams) {
        String url = requestParams.getString("url", "");
        return dealsRepository.getBrandDetails(url);
    }
}

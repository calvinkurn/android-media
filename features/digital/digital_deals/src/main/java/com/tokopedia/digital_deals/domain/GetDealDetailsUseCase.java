package com.tokopedia.digital_deals.domain;

import com.tokopedia.digital_deals.domain.model.dealdetailsdomailmodel.DealsDetailsDomain;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

public class GetDealDetailsUseCase extends UseCase<DealsDetailsDomain> {
    private final DealsRepository dealsRepository;

    public GetDealDetailsUseCase(DealsRepository dealsRepository) {
        super();
        this.dealsRepository = dealsRepository;
    }

    @Override
    public Observable<DealsDetailsDomain> createObservable(RequestParams requestParams) {
        String url = requestParams.getString("url", "");
        return dealsRepository.getDealDetails(url);
    }
}

package com.tokopedia.digital_deals.domain.getusecase;

import com.tokopedia.digital_deals.domain.DealsRepository;
import com.tokopedia.digital_deals.domain.model.dealdetailsdomainmodel.DealsDetailsDomain;
import com.tokopedia.digital_deals.view.presenter.DealDetailsPresenter;
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
        String url = requestParams.getString(DealDetailsPresenter.TAG, "");
        return dealsRepository.getDealDetails(url);
    }
}

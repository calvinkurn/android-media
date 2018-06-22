package com.tokopedia.digital_deals.domain.getusecase;

import com.tokopedia.digital_deals.domain.DealsRepository;
import com.tokopedia.digital_deals.domain.model.branddetailsmodel.BrandDetailsDomain;
import com.tokopedia.digital_deals.view.presenter.BrandDetailsPresenter;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.HashMap;

import rx.Observable;

public class GetBrandDetailsUseCase extends UseCase<BrandDetailsDomain> {
    private final DealsRepository dealsRepository;

    public GetBrandDetailsUseCase(DealsRepository dealsRepository){
        super();
        this.dealsRepository=dealsRepository;
    }

    @Override
    public Observable<BrandDetailsDomain> createObservable(RequestParams requestParams) {
        String url = requestParams.getString(BrandDetailsPresenter.TAG, "");
        HashMap<String, Object> params= requestParams.getParameters();
        params.remove(BrandDetailsPresenter.TAG);
        if(requestParams.getBoolean("search_next", false))
            params=new HashMap<>();
        return dealsRepository.getBrandDetails(url, params);
    }
}

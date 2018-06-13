package com.tokopedia.digital_deals.domain.getusecase;

import com.tokopedia.digital_deals.domain.DealsRepository;
import com.tokopedia.digital_deals.domain.model.allbrandsdomainmodel.AllBrandsDomain;
import com.tokopedia.digital_deals.view.presenter.DealsHomePresenter;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

public class GetAllBrandsUseCase extends UseCase<AllBrandsDomain>{
    private final DealsRepository dealsRepository;

    public GetAllBrandsUseCase(DealsRepository dealsRepository){
        super();
        this.dealsRepository=dealsRepository;
    }
    @Override
    public Observable<AllBrandsDomain> createObservable(RequestParams requestParams) {
        return dealsRepository.getAllBrands(requestParams.getParameters());
    }
}

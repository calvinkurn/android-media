package com.tokopedia.instantloan.domain.interactor;

import com.tokopedia.instantloan.data.repository.InstantLoanDataRepository;
import com.tokopedia.instantloan.domain.model.BannerModelDomain;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by lavekush on 21/03/18.
 */

public class GetBannersUserCase extends UseCase<List<BannerModelDomain>> {

    private final InstantLoanDataRepository mRepository;

    @Inject
    public GetBannersUserCase(InstantLoanDataRepository repository) {
        this.mRepository = repository;
    }

    @Override
    public Observable<List<BannerModelDomain>> createObservable(RequestParams requestParams) {
        return mRepository.getBanners();

    }
}

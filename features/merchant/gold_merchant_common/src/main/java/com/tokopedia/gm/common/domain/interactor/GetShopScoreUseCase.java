package com.tokopedia.gm.common.domain.interactor;

import com.tokopedia.gm.common.data.source.cloud.model.ShopScoreMainDomainModel;
import com.tokopedia.gm.common.domain.repository.GMCommonRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

public class GetShopScoreUseCase extends UseCase<ShopScoreMainDomainModel> {
    private  GMCommonRepository shopScoreRepository;

    @Override
    public Observable<ShopScoreMainDomainModel> createObservable(RequestParams requestParams) {
        return shopScoreRepository.getShopScoreSummary();
    }
}

package com.tokopedia.topads.dashboard.domain.interactor;

import com.tokopedia.usecase.RequestParams;
import com.tokopedia.topads.dashboard.constant.TopAdsNetworkConstant;
import com.tokopedia.topads.dashboard.domain.TopAdsShopAdsRepository;
import com.tokopedia.topads.dashboard.domain.model.TopAdsDetailShopDomainModel;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by zulfikarrahman on 3/10/17.
 */

public class TopAdsCreateDetailShopUseCase extends TopAdsSaveDetailShopUseCase {

    @Inject
    public TopAdsCreateDetailShopUseCase(TopAdsShopAdsRepository topAdsShopAdsRepository) {
        super(topAdsShopAdsRepository);
    }

    @Override
    public Observable<TopAdsDetailShopDomainModel> createObservable(RequestParams requestParams) {
        return topAdsShopAdsRepository.createDetailShop((TopAdsDetailShopDomainModel) requestParams.getObject(TopAdsNetworkConstant.PARAM_AD));
    }
}

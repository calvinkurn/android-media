package com.tokopedia.topads.dashboard.domain.interactor;

import com.tokopedia.topads.dashboard.constant.TopAdsNetworkConstant;
import com.tokopedia.topads.dashboard.domain.TopAdsShopAdsRepository;
import com.tokopedia.topads.dashboard.domain.model.TopAdsDetailShopDomainModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by zulfikarrahman on 2/20/17.
 */
public class TopAdsGetDetailShopUseCase extends UseCase<TopAdsDetailShopDomainModel> {

    private final TopAdsShopAdsRepository topAdsShopAdsRepository;

    @Inject
    public TopAdsGetDetailShopUseCase(TopAdsShopAdsRepository topAdsShopAdsRepository) {
        super();
        this.topAdsShopAdsRepository = topAdsShopAdsRepository;
    }

    @Override
    public Observable<TopAdsDetailShopDomainModel> createObservable(RequestParams requestParams) {
        return topAdsShopAdsRepository.getDetail(requestParams.getString(TopAdsNetworkConstant.PARAM_AD_ID, ""));
    }

    public static RequestParams createRequestParams(String adId){
        RequestParams params = RequestParams.create();
        params.putString(TopAdsNetworkConstant.PARAM_AD_ID, adId);
        return params;
    }
}

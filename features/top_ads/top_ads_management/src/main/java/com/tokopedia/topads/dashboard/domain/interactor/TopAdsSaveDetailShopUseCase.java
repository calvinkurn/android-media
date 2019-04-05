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
public class TopAdsSaveDetailShopUseCase extends UseCase<TopAdsDetailShopDomainModel> {

    protected final TopAdsShopAdsRepository topAdsShopAdsRepository;

    @Inject
    public TopAdsSaveDetailShopUseCase(TopAdsShopAdsRepository topAdsShopAdsRepository) {
        super();
        this.topAdsShopAdsRepository = topAdsShopAdsRepository;
    }

    @Override
    public Observable<TopAdsDetailShopDomainModel> createObservable(RequestParams requestParams) {
        return topAdsShopAdsRepository.saveDetail((TopAdsDetailShopDomainModel) requestParams.getObject(TopAdsNetworkConstant.PARAM_AD));
    }

    public static RequestParams createRequestParams(TopAdsDetailShopDomainModel topAdsDetailShopDomainModel){
        RequestParams params = RequestParams.create();
        params.putObject(TopAdsNetworkConstant.PARAM_AD, topAdsDetailShopDomainModel);
        return params;
    }
}
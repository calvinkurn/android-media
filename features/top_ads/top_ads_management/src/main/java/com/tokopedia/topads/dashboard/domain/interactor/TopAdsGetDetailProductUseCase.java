package com.tokopedia.topads.dashboard.domain.interactor;

import com.tokopedia.topads.dashboard.constant.TopAdsNetworkConstant;
import com.tokopedia.topads.dashboard.domain.TopAdsProductAdsRepository;
import com.tokopedia.topads.dashboard.domain.model.TopAdsDetailProductDomainModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by zulfikarrahman on 2/20/17.
 */
public class TopAdsGetDetailProductUseCase extends UseCase<TopAdsDetailProductDomainModel> {

    private final TopAdsProductAdsRepository topAdsProductAdsRepository;

    @Inject
    public TopAdsGetDetailProductUseCase(TopAdsProductAdsRepository topAdsProductAdsRepository) {
        super();
        this.topAdsProductAdsRepository = topAdsProductAdsRepository;
    }

    @Override
    public Observable<TopAdsDetailProductDomainModel> createObservable(RequestParams requestParams) {
        return topAdsProductAdsRepository.getDetail(requestParams.getString(TopAdsNetworkConstant.PARAM_AD_ID, ""));
    }

    public static RequestParams createRequestParams(String adId){
        RequestParams params = RequestParams.create();
        params.putString(TopAdsNetworkConstant.PARAM_AD_ID, adId);
        return params;
    }
}

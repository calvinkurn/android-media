package com.tokopedia.topads.dashboard.domain.interactor;

import com.tokopedia.topads.common.constant.TopAdsCommonConstant;
import com.tokopedia.topads.dashboard.data.model.data.TotalAd;
import com.tokopedia.topads.dashboard.domain.repository.TopAdsDashboardRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by hadi.putra on 24/04/18.
 */

public class TopAdsPopulateTotalAdsUseCase extends UseCase<TotalAd> {

    private final TopAdsDashboardRepository topAdsDashboardRepository;

    @Inject
    public TopAdsPopulateTotalAdsUseCase(TopAdsDashboardRepository topAdsDashboardRepository) {
        this.topAdsDashboardRepository = topAdsDashboardRepository;
    }

    @Override
    public Observable<TotalAd> createObservable(RequestParams requestParams) {
        return topAdsDashboardRepository.populateTotalAds(requestParams);
    }

    public static RequestParams createRequestParams(String shopId){
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(TopAdsCommonConstant.PARAM_SHOP_ID, shopId);
        return requestParams;
    }
}

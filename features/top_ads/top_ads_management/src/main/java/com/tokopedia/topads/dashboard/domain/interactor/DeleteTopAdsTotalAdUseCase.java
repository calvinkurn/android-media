package com.tokopedia.topads.dashboard.domain.interactor;

import com.tokopedia.cacheapi.domain.interactor.CacheApiDataDeleteUseCase;
import com.tokopedia.topads.common.constant.TopAdsCommonConstant;
import com.tokopedia.topads.dashboard.constant.TopAdsNetworkConstant;
import com.tokopedia.usecase.RequestParams;

import rx.Observable;

/**
 * Created by hadi.putra on 17/05/18.
 */

public class DeleteTopAdsTotalAdUseCase extends CacheApiDataDeleteUseCase {

    public Observable<Boolean> createObservable() {
        return createObservable(RequestParams.create());
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        RequestParams newRequestParams = CacheApiDataDeleteUseCase.createParams(TopAdsCommonConstant.BASE_DOMAIN_URL,
                TopAdsNetworkConstant.PATH_DASHBOARD_TOTAL_AD);
        return super.createObservable(newRequestParams);
    }
}

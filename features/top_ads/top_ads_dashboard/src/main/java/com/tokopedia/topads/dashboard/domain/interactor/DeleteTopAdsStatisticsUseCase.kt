package com.tokopedia.topads.dashboard.domain.interactor

import com.tokopedia.cacheapi.domain.interactor.CacheApiDataDeleteUseCase
import com.tokopedia.topads.common.constant.TopAdsCommonConstant
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.usecase.RequestParams

import rx.Observable

/**
 * Created by hadi.putra on 17/05/18.
 */

class DeleteTopAdsStatisticsUseCase : CacheApiDataDeleteUseCase() {

    fun createObservable(): Observable<Boolean> {
        return createObservable(RequestParams.create())
    }

    override fun createObservable(requestParams: RequestParams): Observable<Boolean> {
        val newRequestParams = CacheApiDataDeleteUseCase.createParams(TopAdsCommonConstant.BASE_DOMAIN_URL,
                TopAdsDashboardConstant.PATH_DASHBOARD_STATISTIC)
        return super.createObservable(newRequestParams)
    }
}

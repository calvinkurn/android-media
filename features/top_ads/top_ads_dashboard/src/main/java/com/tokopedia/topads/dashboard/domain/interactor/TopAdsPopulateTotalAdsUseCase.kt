package com.tokopedia.topads.dashboard.domain.interactor

import com.tokopedia.topads.common.constant.TopAdsCommonConstant
import com.tokopedia.topads.dashboard.data.model.TotalAd
import com.tokopedia.topads.dashboard.domain.repository.TopAdsDashboardRepository
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase

import javax.inject.Inject

import rx.Observable

/**
 * Created by hadi.putra on 24/04/18.
 */

class TopAdsPopulateTotalAdsUseCase @Inject
constructor(private val topAdsDashboardRepository: TopAdsDashboardRepository) : UseCase<TotalAd>() {

    override fun createObservable(requestParams: RequestParams): Observable<TotalAd> {
        return topAdsDashboardRepository.populateTotalAds(requestParams)
    }

    companion object {

        @Inject
        fun createRequestParams(shopId: String): RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putString(TopAdsCommonConstant.PARAM_SHOP_ID, shopId)
            return requestParams
        }
    }
}

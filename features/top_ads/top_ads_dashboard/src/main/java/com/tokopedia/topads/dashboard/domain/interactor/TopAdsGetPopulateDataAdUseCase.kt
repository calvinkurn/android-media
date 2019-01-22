package com.tokopedia.topads.dashboard.domain.interactor

import com.tokopedia.topads.common.constant.TopAdsCommonConstant
import com.tokopedia.topads.common.data.model.DataDeposit
import com.tokopedia.topads.common.domain.interactor.TopAdsGetShopDepositGraphQLUseCase
import com.tokopedia.topads.dashboard.data.model.DashboardPopulateResponse
import com.tokopedia.topads.dashboard.data.model.TotalAd
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase

import javax.inject.Inject

import rx.Observable
import rx.functions.Func2

/**
 * Created by hadi.putra on 18/05/18.
 */

class TopAdsGetPopulateDataAdUseCase @Inject
constructor(private val topAdsPopulateTotalAdsUseCase: TopAdsPopulateTotalAdsUseCase,
            private val topAdsGetShopDepositUseCase: TopAdsGetShopDepositGraphQLUseCase) : UseCase<DashboardPopulateResponse>() {

    override fun createObservable(requestParams: RequestParams): Observable<DashboardPopulateResponse> {
        val depositObservable = topAdsGetShopDepositUseCase.createObservable(requestParams)
        val totalAdsRequestParams = createTotalAdsRequestParams(requestParams.getInt(TopAdsCommonConstant.PARAM_SHOP_ID, 0))
        val totalAdObservable = topAdsPopulateTotalAdsUseCase.createObservable(totalAdsRequestParams)

        return Observable.zip(totalAdObservable, depositObservable) { totalAd, dataDeposit -> DashboardPopulateResponse(totalAd, dataDeposit) }
    }

    private fun createTotalAdsRequestParams(shopId: Int): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putString(TopAdsCommonConstant.PARAM_SHOP_ID, shopId.toString())
        return requestParams
    }

    companion object {
        private val PARAM_QUERY = "query"

        @JvmStatic
        fun createRequestParams(queryDeposit: String, shopId: Int): RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putString(PARAM_QUERY, queryDeposit)
            requestParams.putInt(TopAdsCommonConstant.PARAM_SHOP_ID, shopId)
            return requestParams
        }
    }
}

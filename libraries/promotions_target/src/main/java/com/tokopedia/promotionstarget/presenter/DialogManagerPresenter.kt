package com.tokopedia.promotionstarget.presenter

import com.tokopedia.promotionstarget.data.coupon.GetCouponDetailResponse
import com.tokopedia.promotionstarget.data.pop.GetPopGratificationResponse
import com.tokopedia.promotionstarget.subscriber.GratificationData
import com.tokopedia.promotionstarget.usecase.GetCouponDetailUseCase
import com.tokopedia.promotionstarget.usecase.GetPopGratificationUseCase
import javax.inject.Inject

class DialogManagerPresenter @Inject constructor(val getPopGratificationUseCase: GetPopGratificationUseCase,
                                                 val couponDetailUseCase: GetCouponDetailUseCase) {

    suspend fun getGratificationAndShowDialog(gratificationData: GratificationData): GetPopGratificationResponse {
        val data = getPopGratificationUseCase.let {
            it.getResponse(it.getQueryParams(gratificationData.campaignSlug, gratificationData.page))
        }
        return data
    }


    suspend fun composeApi(gratificationData: GratificationData): GetCouponDetailResponse {
        val data = getGratificationAndShowDialog(gratificationData)
        val ids = mapperGratificationResponseToCouponIds(data)
        return getCatalogDetail(ids)
    }

    //todo Rahul avoid !!
    private fun mapperGratificationResponseToCouponIds(response: GetPopGratificationResponse): List<String> {
        val ids = response.popGratification?.popGratificationBenefits?.map {
            it?.referenceID.toString()
        }
        return ids!!
    }

    private suspend fun getCatalogDetail(ids: List<String>): GetCouponDetailResponse {
        return couponDetailUseCase.getResponse(ids)
    }

}
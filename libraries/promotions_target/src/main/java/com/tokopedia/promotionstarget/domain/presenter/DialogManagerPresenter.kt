package com.tokopedia.promotionstarget.domain.presenter

import com.tokopedia.promotionstarget.data.coupon.GetCouponDetailResponse
import com.tokopedia.promotionstarget.data.pop.GetPopGratificationResponse
import com.tokopedia.promotionstarget.presentation.subscriber.GratificationData
import com.tokopedia.promotionstarget.domain.usecase.GetCouponDetailUseCase
import com.tokopedia.promotionstarget.domain.usecase.GetPopGratificationUseCase
import javax.inject.Inject

class DialogManagerPresenter @Inject constructor(val getPopGratificationUseCase: GetPopGratificationUseCase,
                                                 val couponDetailUseCase: GetCouponDetailUseCase) {

    suspend fun getGratificationAndShowDialog(gratificationData: GratificationData): GetPopGratificationResponse {
        val data = getPopGratificationUseCase.let {
            it.getResponse(it.getQueryParams(gratificationData.popSlug, gratificationData.page))
        }
        return data
    }


    suspend fun composeApi(getPopGratificationResponse: GetPopGratificationResponse): GetCouponDetailResponse {
        val ids = mapperGratificationResponseToCouponIds(getPopGratificationResponse)
        return getCatalogDetail(ids)
    }

    private fun mapperGratificationResponseToCouponIds(response: GetPopGratificationResponse): List<String> {
        var ids = response.popGratification?.popGratificationBenefits?.map {
            it?.referenceID.toString()
        }
        if (ids == null) {
            ids = emptyList()
        }
        return ids
    }

    private suspend fun getCatalogDetail(ids: List<String>): GetCouponDetailResponse {
        return couponDetailUseCase.getResponse(ids)
    }

}
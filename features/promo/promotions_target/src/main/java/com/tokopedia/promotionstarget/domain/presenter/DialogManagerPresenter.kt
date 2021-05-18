package com.tokopedia.promotionstarget.domain.presenter

import androidx.annotation.VisibleForTesting
import com.tokopedia.promotionstarget.data.claim.ClaimPayload
import com.tokopedia.promotionstarget.data.claim.ClaimPopGratificationResponse
import com.tokopedia.promotionstarget.data.coupon.GetCouponDetailResponse
import com.tokopedia.promotionstarget.data.pop.GetPopGratificationResponse
import com.tokopedia.promotionstarget.data.pop.PopGratificationBenefitsItem
import com.tokopedia.promotionstarget.domain.usecase.ClaimPopGratificationUseCase
import com.tokopedia.promotionstarget.domain.usecase.GetCouponDetailUseCase
import com.tokopedia.promotionstarget.domain.usecase.GetPopGratificationUseCase
import com.tokopedia.promotionstarget.presentation.subscriber.GratificationData
import com.tokopedia.user.session.UserSession
import javax.inject.Inject

class DialogManagerPresenter @Inject constructor(
        val userSession: UserSession,
        val getPopGratificationUseCase: GetPopGratificationUseCase,
        val couponDetailUseCase: GetCouponDetailUseCase,
        val claimPopGratificationUseCase: ClaimPopGratificationUseCase
) {

    suspend fun getGratificationAndShowDialog(gratificationData: GratificationData): GetPopGratificationResponse {
        val data = getPopGratificationUseCase.let {
            it.getResponse(it.getQueryParams(gratificationData.popSlug, gratificationData.page))
        }
        return data
    }

    suspend fun claimGratification(claimPayload: ClaimPayload): ClaimPopGratificationResponse {
        return claimPopGratificationUseCase.getResponse(claimPopGratificationUseCase.getQueryParams(claimPayload))
    }


    suspend fun composeApi(popGratificationBenefits: List<PopGratificationBenefitsItem?>?): GetCouponDetailResponse {
        val ids = mapperGratificationResponseToCouponIds(popGratificationBenefits)
        return getCatalogDetail(ids)
    }

    @VisibleForTesting
    fun mapperGratificationResponseToCouponIds(popGratificationBenefits: List<PopGratificationBenefitsItem?>?): List<String> {
        val ids = arrayListOf<String>()
        popGratificationBenefits?.forEach {
            if(it?.referenceID!=null){
                ids.add(it.referenceID.toString())
            }
        }
        return ids
    }

    @VisibleForTesting
    suspend fun getCatalogDetail(ids: List<String>): GetCouponDetailResponse {
        return couponDetailUseCase.getResponse(ids)
    }

}
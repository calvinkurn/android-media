package com.tokopedia.discovery2.usecase.quickcouponusecase

import com.tokopedia.discovery2.data.quickcouponresponse.ApplyCouponResponse
import com.tokopedia.discovery2.data.quickcouponresponse.PhoneVerificationResponse
import com.tokopedia.discovery2.data.quickcouponresponse.QuickCouponDetailResponse
import com.tokopedia.discovery2.repository.quickcoupon.QuickCouponRepository
import javax.inject.Inject

class QuickCouponUseCase @Inject constructor(private val quickCouponRepository: QuickCouponRepository) {

    suspend fun getCouponDetail(pageEndPoint: String): QuickCouponDetailResponse {
        return quickCouponRepository.getQuickCouponDetailData(pageEndPoint)
    }

    suspend fun getMobileVerificationStatus(): PhoneVerificationResponse {
        return quickCouponRepository.getMobileVerificationData()
    }

    suspend fun applyQuickCoupon(promoCode: String): ApplyCouponResponse {
        return quickCouponRepository.applyUserQuickCoupon(promoCode)
    }
}
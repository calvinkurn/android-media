package com.tokopedia.discovery2.repository.quickcoupon

import com.tokopedia.discovery2.data.quickcouponresponse.ApplyCouponResponse
import com.tokopedia.discovery2.data.quickcouponresponse.PhoneVerificationResponse
import com.tokopedia.discovery2.data.quickcouponresponse.QuickCouponDetailResponse

interface QuickCouponRepository {
    suspend fun getQuickCouponDetailData(pageIdentifier: String) : QuickCouponDetailResponse
    suspend fun getMobileVerificationData() : PhoneVerificationResponse
    suspend fun applyUserQuickCoupon(promoCode: String): ApplyCouponResponse
}
package com.tokopedia.discovery2.repository.claimCoupon

import com.tokopedia.discovery2.data.claim_coupon.ClaimCouponRequest
import com.tokopedia.discovery2.data.claim_coupon.ClaimCouponResponse
import com.tokopedia.discovery2.data.claimcoupon.RedeemCouponResponse

interface IClaimCouponGqlRepository {
    suspend fun getClickCouponData(claimCouponRequest: ClaimCouponRequest) : ClaimCouponResponse
    suspend fun redeemCoupon(mapOf: Map<String, Any>): RedeemCouponResponse
}

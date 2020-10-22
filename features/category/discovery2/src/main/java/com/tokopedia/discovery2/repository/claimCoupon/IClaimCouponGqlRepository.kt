package com.tokopedia.discovery2.repository.claimCoupon

import com.tokopedia.discovery2.data.claimcoupon.RedeemCouponResponse

interface IClaimCouponGqlRepository {
    suspend fun redeemCoupon(mapOf: Map<String, Any>): RedeemCouponResponse
}
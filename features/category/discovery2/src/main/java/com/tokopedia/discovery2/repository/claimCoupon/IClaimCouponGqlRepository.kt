package com.tokopedia.discovery2.repository.claimCoupon

import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.claimcoupon.RedeemCouponResponse

interface IClaimCouponGqlRepository {
    suspend fun getClickCouponData(componentId: String, pageEndPoint: String) : ArrayList<ComponentsItem>
    suspend fun redeemCoupon(mapOf: Map<String, Any>): RedeemCouponResponse
}
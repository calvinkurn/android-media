package com.tokopedia.discovery2.repository.mycoupon

import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.claimcoupon.RedeemCouponResponse

interface MyCouponRepository {
    suspend fun getCouponData(componentId: String,queryParamterMap: MutableMap<String, Any>, pageEndPoint: String) : ArrayList<ComponentsItem>
}
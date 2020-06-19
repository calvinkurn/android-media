package com.tokopedia.discovery2.repository.claimCoupon

import com.tokopedia.discovery2.data.ComponentsItem

interface IClaimCouponRepository {
    suspend fun getClickCouponData(url: String): ArrayList<ComponentsItem>
}
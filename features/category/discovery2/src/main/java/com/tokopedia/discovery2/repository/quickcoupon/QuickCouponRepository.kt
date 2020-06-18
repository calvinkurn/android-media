package com.tokopedia.discovery2.repository.quickcoupon

import com.tokopedia.discovery2.discoverymapper.DiscoveryDataMapper

interface QuickCouponRepository {
    suspend fun getQuickCouponDetailData(pageIdentifier: String)
}
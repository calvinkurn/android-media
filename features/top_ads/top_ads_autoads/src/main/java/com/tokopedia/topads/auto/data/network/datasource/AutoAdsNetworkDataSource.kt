package com.tokopedia.topads.auto.data.network.datasource

import android.arch.lifecycle.LiveData
import com.tokopedia.topads.auto.data.network.response.TopAdsAutoAdsResponse
import com.tokopedia.topads.auto.data.network.response.TopAdsShopInfoResponse
import com.tokopedia.topads.auto.data.network.response.TopadsBidInfoResponse
import com.tokopedia.topads.auto.internal.TopAdsPostAutoAdsParam

/**
 * Author errysuprayogi on 16,May,2019
 */
interface AutoAdsNetworkDataSource {

    val shopInfo: LiveData<TopAdsShopInfoResponse>
    val bidInfo: LiveData<TopadsBidInfoResponse>
    val autoAds: LiveData<TopAdsAutoAdsResponse>

    suspend fun getShopInfo(
            shopId: Int
    )

    suspend fun getBidInfo(
            shopId: Int,
            requestType: String,
            source: String
    )

    suspend fun getAutoAds(
            shopId: Int
    )

    suspend fun postAutoAds(
            input: TopAdsPostAutoAdsParam
    )
}

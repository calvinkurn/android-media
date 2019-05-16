package com.tokopedia.topads.auto.data.repository

import android.arch.lifecycle.LiveData
import com.tokopedia.topads.auto.data.network.response.TopAdsAutoAdsResponse
import com.tokopedia.topads.auto.data.network.response.TopAdsShopInfoResponse
import com.tokopedia.topads.auto.data.network.response.TopadsBidInfoResponse
import com.tokopedia.topads.auto.internal.TopAdsPostAutoAdsParam

/**
 * Author errysuprayogi on 16,May,2019
 */
interface AutoTopAdsRepositoy {

    suspend fun getAdsShopInfo(shopId: Int): LiveData<TopAdsShopInfoResponse>

    suspend fun getDailyBudget(shopId: Int, requestType: String, source: String): LiveData<TopadsBidInfoResponse>

    suspend fun getAutoAds(shopId: Int): LiveData<TopAdsAutoAdsResponse>

    suspend fun postAutoAds(input: TopAdsPostAutoAdsParam): LiveData<TopAdsAutoAdsResponse>

}
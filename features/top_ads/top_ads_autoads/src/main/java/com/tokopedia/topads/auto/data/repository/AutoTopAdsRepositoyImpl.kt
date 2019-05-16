package com.tokopedia.topads.auto.data.repository

import android.arch.lifecycle.LiveData
import com.tokopedia.topads.auto.data.network.datasource.AutoAdsNetworkDataSource
import com.tokopedia.topads.auto.data.network.response.TopAdsAutoAdsResponse
import com.tokopedia.topads.auto.data.network.response.TopAdsShopInfoResponse
import com.tokopedia.topads.auto.data.network.response.TopadsBidInfoResponse
import com.tokopedia.topads.auto.internal.TopAdsPostAutoAdsParam

class AutoTopAdsRepositoyImpl(
        private val dataSource: AutoAdsNetworkDataSource
) : AutoTopAdsRepositoy {

    override suspend fun getAdsShopInfo(shopId: Int): LiveData<TopAdsShopInfoResponse> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getDailyBudget(shopId: Int, requestType: String, source: String): LiveData<TopadsBidInfoResponse> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getAutoAds(shopId: Int): LiveData<TopAdsAutoAdsResponse> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun postAutoAds(input: TopAdsPostAutoAdsParam): LiveData<TopAdsAutoAdsResponse> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
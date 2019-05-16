package com.tokopedia.topads.auto.data.network.datasource

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.topads.auto.data.network.response.TopAdsAutoAdsResponse
import com.tokopedia.topads.auto.data.network.response.TopAdsShopInfoResponse
import com.tokopedia.topads.auto.data.network.response.TopadsBidInfoResponse
import com.tokopedia.topads.auto.internal.TopAdsPostAutoAdsParam
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class AutoAdsNetworkDataSourceImpl @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                   private val userSessionInterface: UserSessionInterface,
                                   private val rawQueries: Map<String, String>) : AutoAdsNetworkDataSource {

    private val mutableShopInfo = MutableLiveData<TopAdsShopInfoResponse>()
    private val mutableBidInfo = MutableLiveData<TopadsBidInfoResponse>()
    private val mutableAutoAds = MutableLiveData<TopAdsAutoAdsResponse>()

    override val shopInfo: LiveData<TopAdsShopInfoResponse>
        get() = mutableShopInfo
    override val bidInfo: LiveData<TopadsBidInfoResponse>
        get() = mutableBidInfo
    override val autoAds: LiveData<TopAdsAutoAdsResponse>
        get() = mutableAutoAds

    override suspend fun getShopInfo(shopId: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getBidInfo(shopId: Int, requestType: String, source: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getAutoAds(shopId: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun postAutoAds(input: TopAdsPostAutoAdsParam) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
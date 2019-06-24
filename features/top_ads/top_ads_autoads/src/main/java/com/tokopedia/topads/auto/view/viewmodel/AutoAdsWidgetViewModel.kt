package com.tokopedia.topads.auto.view.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.topads.auto.data.entity.TopAdsAutoAdsData
import com.tokopedia.topads.auto.data.network.response.TopAdsAutoAds
import com.tokopedia.topads.auto.internal.RawQueryKeyObject
import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.withContext

/**
 * Author errysuprayogi on 15,May,2019
 */
class AutoAdsWidgetViewModel(
        private val dispatcher: CoroutineDispatcher,
        private val repository: GraphqlRepository,
        private val rawQueries: Map<String, String>
) : BaseViewModel(dispatcher) {

    val autoAdsData = MutableLiveData<TopAdsAutoAdsData>()

    fun getAutoAdsStatus(shopId: Int) {
        launchCatchError(block = {
            val data = withContext(Dispatchers.IO) {
                val request = GraphqlRequest(rawQueries[RawQueryKeyObject.QUERY_GET_AUTO_ADS],
                        TopAdsAutoAds.Response::class.java, mapOf(SHOP_ID to shopId))
                val cacheStrategy = GraphqlCacheStrategy
                        .Builder(CacheType.ALWAYS_CLOUD).build()
                repository.getReseponse(listOf(request), cacheStrategy)
            }
            data.getSuccessData<TopAdsAutoAds.Response>().autoAds.data.let {
                autoAdsData.postValue(it)
            }
        }) {
            it.printStackTrace()
        }
    }

    companion object {
        val SHOP_ID = "shopId"
    }

}

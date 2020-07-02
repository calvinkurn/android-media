package com.tokopedia.topads.auto.view.viewmodel

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.topads.auto.data.entity.TopAdsAutoAdsData
import com.tokopedia.topads.auto.data.network.param.AutoAdsParam
import com.tokopedia.topads.auto.data.network.response.NonDeliveryResponse
import com.tokopedia.topads.auto.data.network.response.TopAdsAutoAds
import com.tokopedia.topads.auto.internal.RawQueryKeyObject
import com.tokopedia.topads.common.data.util.Utils
import com.tokopedia.usecase.RequestParams
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONException

/**
 * Author errysuprayogi on 15,May,2019
 */
class AutoAdsWidgetViewModel(
        private val dispatcher: CoroutineDispatcher,
        private val repository: GraphqlRepository,
        private val rawQueries: Map<String, String>
) : BaseViewModel(dispatcher) {

    val autoAdsData = MutableLiveData<TopAdsAutoAdsData>()
    val autoAdsStatus = MutableLiveData<TopAdsAutoAdsData>()
    val adsDeliveryStatus = MutableLiveData<NonDeliveryResponse.TopAdsGetShopStatus.DataItem>()

    fun getAutoAdsStatus(shopId: Int) {
        launchCatchError(block = {
            val data = withContext(Dispatchers.IO) {
                val request = GraphqlRequest(rawQueries[RawQueryKeyObject.QUERY_GET_AUTO_ADS],
                        TopAdsAutoAds.Response::class.java, mapOf(SHOP_ID to shopId), false)
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

    fun postAutoAds(param: AutoAdsParam) {
        launchCatchError(block = {
            val data = withContext(Dispatchers.IO) {
                val request = GraphqlRequest(rawQueries[RawQueryKeyObject.QUERY_POST_AUTO_ADS],
                        TopAdsAutoAds.Response::class.java, getParams(param).parameters)
                val cacheStrategy = GraphqlCacheStrategy
                        .Builder(CacheType.ALWAYS_CLOUD).build()
                repository.getReseponse(listOf(request), cacheStrategy)
            }
            data.getSuccessData<TopAdsAutoAds.Response>().autoAds.data.let {
                autoAdsStatus.postValue(it)
            }
        }) {
            it.printStackTrace()
        }
    }

    fun getNotDeliveredReason(shopID: String) {
        launchCatchError(block = {
            val data = withContext(Dispatchers.IO) {
                val request = GraphqlRequest(rawQueries[RawQueryKeyObject.QUERY_TOPADS_NONDELIVERY_REASON],
                        NonDeliveryResponse::class.java, mapOf(SHOPID to shopID, ADTYPE to "1"), false)
                val cacheStrategy = GraphqlCacheStrategy
                        .Builder(CacheType.ALWAYS_CLOUD).build()
                repository.getReseponse(listOf(request), cacheStrategy)
            }
            data.getSuccessData<NonDeliveryResponse>().topAdsGetShopStatus.data.let {
                adsDeliveryStatus.postValue(it[0])
            }
        }) {
            it.printStackTrace()
        }
    }

    fun getParams(dataParams: AutoAdsParam): RequestParams {
        val params = RequestParams.create()
        try {
            params.putAll(Utils.jsonToMap(Gson().toJson(dataParams)))
        } catch (e: JSONException) {
            e.printStackTrace()
        } finally {
            return params
        }
    }

    companion object {
        val SHOP_ID = "shopId"
        val SHOPID = "shopID"
        val ADTYPE = "adTypes"
    }

}

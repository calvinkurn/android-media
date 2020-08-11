package com.tokopedia.topads.auto.view.viewmodel

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.topads.auto.data.entity.TopAdsAutoAdsData
import com.tokopedia.topads.auto.data.network.param.AutoAdsParam
import com.tokopedia.topads.auto.data.network.response.NonDeliveryResponse
import com.tokopedia.topads.auto.data.network.response.TopAdsAutoAds
import com.tokopedia.topads.auto.di.AutoAdsDispatcherProvider
import com.tokopedia.topads.auto.internal.RawQueryKeyObject
import com.tokopedia.topads.auto.view.RequestHelper
import com.tokopedia.topads.common.data.util.Utils
import com.tokopedia.usecase.RequestParams
import kotlinx.coroutines.withContext
import org.json.JSONException

/**
 * Author errysuprayogi on 15,May,2019
 */
class AutoAdsWidgetViewModel(
        private val dispatcher: AutoAdsDispatcherProvider,
        private val repository: GraphqlRepository,
        private val rawQueries: Map<String, String>
) : BaseViewModel(dispatcher.ui()) {

    val autoAdsData = MutableLiveData<TopAdsAutoAdsData>()
    val autoAdsStatus = MutableLiveData<TopAdsAutoAdsData>()
    val adsDeliveryStatus = MutableLiveData<NonDeliveryResponse.TopAdsGetShopStatus.DataItem>()

    fun getAutoAdsStatus(shopId: Int) {
        launchCatchError(block = {
            val data = withContext(dispatcher.io()) {
                val request = RequestHelper.getGraphQlRequest(rawQueries[RawQueryKeyObject.QUERY_GET_AUTO_ADS],
                        TopAdsAutoAds.Response::class.java, hashMapOf(SHOP_ID to shopId))
                val cacheStrategy = RequestHelper.getCacheStrategy()
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
            val data = withContext(dispatcher.io()) {
                val request = RequestHelper.getGraphQlRequest(rawQueries[RawQueryKeyObject.QUERY_POST_AUTO_ADS],
                        TopAdsAutoAds.Response::class.java, getParams(param).parameters)

                val cacheStrategy = RequestHelper.getCacheStrategy()
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
            val data = withContext(dispatcher.io()) {
                val request = RequestHelper.getGraphQlRequest(rawQueries[RawQueryKeyObject.QUERY_TOPADS_NONDELIVERY_REASON],
                        NonDeliveryResponse::class.java, hashMapOf(SHOPID to shopID, ADTYPE to "1"))
                val cacheStrategy = RequestHelper.getCacheStrategy()
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
        const val SHOP_ID = "shopId"
        const val SHOPID = "shopID"
        const val ADTYPE = "adTypes"
    }

}

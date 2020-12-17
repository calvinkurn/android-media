package com.tokopedia.topads.common.view

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.topads.common.R
import com.tokopedia.topads.common.data.model.AutoAdsParam
import com.tokopedia.topads.common.data.response.NonDeliveryResponse
import com.tokopedia.topads.common.data.response.TopAdsAutoAds
import com.tokopedia.topads.common.data.response.TopAdsAutoAdsData
import com.tokopedia.topads.common.data.util.Utils
import com.tokopedia.topads.common.di.ActivityContext
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.json.JSONException
import javax.inject.Inject

/**
 * Created by Pika on 18/9/20.
 */

class AutoAdsWidgetViewModelCommon @Inject constructor(
        private val dispatcher: CoroutineDispatcher,
        private val repository: GraphqlRepository,
        @ActivityContext private val context: Context
) : BaseViewModel(dispatcher) {

    val autoAdsData = MutableLiveData<TopAdsAutoAdsData>()
    val autoAdsStatus = MutableLiveData<TopAdsAutoAdsData>()
    val adsDeliveryStatus = MutableLiveData<NonDeliveryResponse.TopAdsGetShopStatus.DataItem>()

    fun getAutoAdsStatus(shopId: Int) {
        launchCatchError(block = {
            val data = withContext(dispatcher) {
                val request = GraphqlRequest(GraphqlHelper.loadRawString(context.resources, R.raw.query_auto_ads_status),
                        TopAdsAutoAds.Response::class.java, mapOf(SHOP_ID to shopId))
                val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.CLOUD_THEN_CACHE).build()

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
            val data = withContext(dispatcher) {
                val request = GraphqlRequest(GraphqlHelper.loadRawString(context.resources,R.raw.topads_common_query_post_autoads),
                        TopAdsAutoAds.Response::class.java, getParams(param).parameters)
                val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.CLOUD_THEN_CACHE).build()
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
            val data = withContext(dispatcher) {
                val request =  GraphqlRequest(GraphqlHelper.loadRawString(context.resources,R.raw.topads_common_auto_query_get_nondelivery_reason),
                        NonDeliveryResponse::class.java, mapOf(SHOPID to shopID, ADTYPE to "1"))
                val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.CLOUD_THEN_CACHE).build()
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

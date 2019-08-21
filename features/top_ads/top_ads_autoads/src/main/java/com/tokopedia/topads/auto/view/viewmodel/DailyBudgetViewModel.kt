package com.tokopedia.topads.auto.view.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.content.Context
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.topads.auto.R
import com.tokopedia.topads.auto.data.entity.BidInfoData
import com.tokopedia.topads.auto.data.entity.TopAdsAutoAdsData
import com.tokopedia.topads.auto.data.entity.TopAdsShopInfoData
import com.tokopedia.topads.auto.data.network.param.AutoAdsParam
import com.tokopedia.topads.auto.data.network.response.TopAdsAutoAds
import com.tokopedia.topads.auto.data.network.response.TopAdsShopInfo
import com.tokopedia.topads.auto.data.network.response.TopadsBidInfo
import com.tokopedia.topads.auto.internal.RawQueryKeyObject
import com.tokopedia.topads.common.data.util.Utils
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import org.json.JSONException
import javax.inject.Inject

/**
 * Author errysuprayogi on 09,May,2019
 */
class DailyBudgetViewModel @Inject constructor(
        private val context: Context,
        private val dispatcher: CoroutineDispatcher,
        private val repository: GraphqlRepository,
        private val rawQueries: Map<String, String>
        ) : BaseViewModel(dispatcher) {

    val budgetInfoData = MutableLiveData<List<BidInfoData>>()
    val autoAdsData = MutableLiveData<TopAdsAutoAdsData>()
    val shopInfoData = MutableLiveData<TopAdsShopInfoData>()

    fun getBudgetInfo(shopId: Int, requestType: String, source: String) {
        launchCatchError(block = {
            val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
            val data = withContext(Dispatchers.IO){
                val request = GraphqlRequest(rawQueries[RawQueryKeyObject.QUERY_ADS_BID_INFO],
                        TopadsBidInfo.Response::class.java,
                        mapOf(SHOP_ID to shopId, REQUEST_TYPE to requestType, SOURCE to source))
                repository.getReseponse(listOf(request), cacheStrategy)
            }

            val request = GraphqlRequest(rawQueries[RawQueryKeyObject.QUERY_ADS_SHOP_INFO],
                    TopAdsShopInfo.Response::class.java, mapOf(SHOP_ID to shopId))
            val shopInfo = repository.getReseponse(listOf(request), cacheStrategy)
                    .getSuccessData<TopAdsShopInfo.Response>().shopInfo

            data.getSuccessData<TopadsBidInfo.Response>().bidInfo.data.let {
                it.forEach {it.shopStatus = shopInfo.data.category }
                budgetInfoData.postValue(it)
            }
        }) {
            it.printStackTrace()
        }
    }


    fun postAutoAds(param : AutoAdsParam){
        launchCatchError(block = {
            val data = withContext(Dispatchers.IO) {
                val request = GraphqlRequest(rawQueries[RawQueryKeyObject.QUERY_POST_AUTO_ADS],
                        TopAdsAutoAds.Response::class.java, getParams(param).parameters)
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


    fun getPotentialImpression(minBid: Double, maxBid: Double, bid: Double): String {
        return String.format("%,.0f - %,.0f", calculateImpression(maxBid, bid),
                calculateImpression(minBid, bid))
    }

    private fun calculateImpression(bid: Double, `val`: Double): Double {
        return 100 / 2.5 * (`val` / bid)
    }

    fun checkBudget(number: Double, minDailyBudget: Double, maxDailyBudget: Double): String? {
        return if (number <= 0) {
            context.getString(R.string.error_empty_budget)
        } else if (number < minDailyBudget) {
            String.format(context.getString(R.string.error_minimum_budget), minDailyBudget)
        } else if (number > maxDailyBudget) {
            String.format(context.getString(R.string.error_maximum_budget), maxDailyBudget)
        } else if (number < maxDailyBudget && number > minDailyBudget && number % BUDGET_MULTIPLE_BY != 0.0) {
            context.getString(R.string.error_multiply_budget, BUDGET_MULTIPLE_BY.toString())
        } else {
            null
        }
    }

    companion object {
        val BUDGET_MULTIPLE_BY = 1000.0
        val SHOP_ID = "shopId"
        val REQUEST_TYPE = "requestType"
        val SOURCE = "source"
    }
}

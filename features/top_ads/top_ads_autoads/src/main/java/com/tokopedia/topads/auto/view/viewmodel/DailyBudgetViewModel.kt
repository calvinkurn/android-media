package com.tokopedia.topads.auto.view.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.topads.auto.R
import com.tokopedia.topads.auto.data.entity.TopAdsAutoAdsData
import com.tokopedia.topads.auto.data.network.param.AutoAdsParam
import com.tokopedia.topads.auto.data.network.response.EstimationResponse
import com.tokopedia.topads.auto.data.network.response.TopAdsAutoAds
import com.tokopedia.topads.auto.data.network.response.TopAdsDepositResponse
import com.tokopedia.topads.auto.data.network.response.TopadsBidInfo
import com.tokopedia.topads.auto.di.AutoAdsDispatcherProvider
import com.tokopedia.topads.auto.internal.RawQueryKeyObject
import com.tokopedia.topads.auto.internal.RawQueryKeyObject.QUERY_TOPADS_DEPOSIT
import com.tokopedia.topads.auto.view.RequestHelper
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.internal.ParamObject.IDS
import com.tokopedia.topads.common.data.internal.ParamObject.PRODUCT
import com.tokopedia.topads.common.data.util.Utils
import com.tokopedia.usecase.RequestParams
import kotlinx.coroutines.withContext
import org.json.JSONException
import java.util.*
import javax.inject.Inject

/**
 * Author errysuprayogi on 09,May,2019
 */
class DailyBudgetViewModel @Inject constructor(
        private val context: Context,
        private val dispatcher: AutoAdsDispatcherProvider,
        private val repository: GraphqlRepository,
        private val rawQueries: Map<String, String>
) : BaseViewModel(dispatcher.ui()) {

    val autoAdsData = MutableLiveData<TopAdsAutoAdsData>()
    val topAdsDeposit = MutableLiveData<Int>()

    fun getBudgetInfo(shopId: Int, requestType: String, source: String, onSuccess: (TopadsBidInfo.Response) -> Unit) {
        launchCatchError(block = {
            val dummyId: MutableList<Int> = mutableListOf()
            val map = mapOf(TYPE to PRODUCT, IDS to dummyId)
            val cacheStrategy = RequestHelper.getCacheStrategy()
            val data = withContext(dispatcher.io()) {
                val request = RequestHelper.getGraphQlRequest(rawQueries[RawQueryKeyObject.QUERY_ADS_BID_INFO],
                        TopadsBidInfo.Response::class.java,
                        hashMapOf(ParamObject.SUGGESTION to map, SHOP_ID to shopId, REQUEST_TYPE to requestType, SOURCE to source))
                repository.getReseponse(listOf(request), cacheStrategy)
            }
            onSuccess(data.getSuccessData<TopadsBidInfo.Response>())
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
                autoAdsData.postValue(it)
            }
        }) {
            it.printStackTrace()
        }
    }

    fun getTopAdsDeposit(shopId: Int) {
        launchCatchError(
                block = {
                    val param: HashMap<String, Any> = hashMapOf(SHOP_ID to shopId,
                            CREDIT_DATA to "unclaimed", SHOP_DATA to "0")
                    val data = withContext(dispatcher.io()) {
                        val request = RequestHelper.getGraphQlRequest(rawQueries[QUERY_TOPADS_DEPOSIT],
                                TopAdsDepositResponse.Data::class.java,
                                param)
                        val cacheStrategy = RequestHelper.getCacheStrategy()
                        repository.getReseponse(listOf(request), cacheStrategy)
                    }
                    data.getSuccessData<TopAdsDepositResponse.Data>().let {
                        topAdsDeposit.postValue(it.topadsDashboardDeposits.data.amount)
                    }
                }) {
            it.printStackTrace()
        }
    }

    fun topadsStatisticsEstimationPotentialReach(onSuccess: (EstimationResponse.TopadsStatisticsEstimationAttribute.DataItem) -> Unit, shopId: String, source: String) {
        launchCatchError(block = {
            val data = withContext(dispatcher.io()) {
                val request = RequestHelper.getGraphQlRequest(rawQueries[RawQueryKeyObject.QUERY_POTENTIAL_REACH_ESTIMATION],
                        EstimationResponse::class.java, hashMapOf(SHOP_ID to shopId, TYPE to 1, SOURCE to source))
                val cacheStrategy = RequestHelper.getCacheStrategy()
                repository.getReseponse(listOf(request), cacheStrategy)
            }
            onSuccess(data.getSuccessData<EstimationResponse>().topadsStatisticsEstimationAttribute.data[0])
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

    fun getPotentialImpressionGQL(budget: Int, lowClickDivider: Int): String {
        return String.format("%,.0f", (budget / lowClickDivider).toDouble())
    }

    fun checkBudget(number: Double, minDailyBudget: Double, maxDailyBudget: Double): String? {
        return when {
            number <= 0 ->
                context.getString(R.string.error_empty_budget)
            number < minDailyBudget ->
                String.format(context.getString(R.string.error_minimum_budget), minDailyBudget)
            number > maxDailyBudget ->
                String.format(context.getString(R.string.error_maximum_budget), maxDailyBudget)
            number < maxDailyBudget && number > minDailyBudget && number % BUDGET_MULTIPLE_BY != 0.0 ->
                context.getString(R.string.error_multiply_budget, BUDGET_MULTIPLE_BY.toString())
            else ->
                null
        }
    }

    companion object {
        const val BUDGET_MULTIPLE_BY = 1000.0
        const val SHOP_ID = "shopId"
        const val REQUEST_TYPE = "requestType"
        const val SOURCE = "source"
        const val TYPE = "type"
        const val CREDIT_DATA = "creditData"
        const val SHOP_DATA = "shopData"
    }
}

package com.tokopedia.topads.auto.view.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.topads.auto.R
import com.tokopedia.topads.auto.data.network.response.EstimationResponse
import com.tokopedia.topads.auto.internal.RawQueryKeyObject
import com.tokopedia.topads.auto.view.RequestHelper
import com.tokopedia.topads.common.data.internal.ParamObject.PRODUCT
import com.tokopedia.topads.common.data.model.AutoAdsParam
import com.tokopedia.topads.common.data.model.DataSuggestions
import com.tokopedia.topads.common.data.response.ResponseBidInfo
import com.tokopedia.topads.common.data.response.TopAdsAutoAds
import com.tokopedia.topads.common.data.response.TopAdsAutoAdsData
import com.tokopedia.topads.common.data.util.Utils
import com.tokopedia.topads.common.domain.interactor.BidInfoUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsGetDepositUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsQueryPostAutoadsUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import org.json.JSONException
import javax.inject.Inject

/**
 * Author errysuprayogi on 09,May,2019
 */
class DailyBudgetViewModel @Inject constructor(
    private val context: Context,
    private val dispatcher: CoroutineDispatchers,
    private val repository: GraphqlRepository,
    private val rawQueries: Map<String, String>,
    private val topAdsGetShopDepositUseCase: TopAdsGetDepositUseCase,
    private val bidInfoUseCase: BidInfoUseCase,
    private val queryPostAutoadsUseCase: TopAdsQueryPostAutoadsUseCase
) : BaseViewModel(dispatcher.main) {

    val autoAdsData : MutableLiveData<Result<TopAdsAutoAdsData>> = MutableLiveData()
    private val topAdsDeposit: MutableLiveData<Int> = MutableLiveData()
    fun getTopAdsDepositLiveData(): LiveData<Int> = topAdsDeposit

    fun getBudgetInfo(
        requestType: String,
        source: String,
        onSuccess: (ResponseBidInfo.Result) -> Unit,
    ) {
        launchCatchError(block = {
            val dummyId: MutableList<String> = mutableListOf()
            val suggestionsDefault = ArrayList<DataSuggestions>()
            suggestionsDefault.add(DataSuggestions(PRODUCT, dummyId))
            bidInfoUseCase.setParams(suggestionsDefault, requestType, source)
            bidInfoUseCase.executeQuerySafeMode({
                onSuccess(it)
            }, {
                it.printStackTrace()
            })
        }) {
            it.printStackTrace()
        }
    }

    fun postAutoAds(param: AutoAdsParam) {
        queryPostAutoadsUseCase.setParam(param).execute(
            onSuccess = { data ->
                autoAdsData.postValue(
                    if(data.autoAds.data != null) {
                        Success(data = data.autoAds.data!!)
                    } else {
                        Fail(Throwable(data.autoAds.error.firstOrNull()?.detail))
                    }
                )
            }, onError = {
                it.printStackTrace()
            }
        )
    }

    fun getTopAdsDeposit() {
        launchCatchError(
            block = {
                val response = topAdsGetShopDepositUseCase.executeOnBackground()
                topAdsDeposit.value = response.topadsDashboardDeposits.data.amount
            },
            onError = {
                it.printStackTrace()
            })
    }

    fun topadsStatisticsEstimationPotentialReach(
        onSuccess: (EstimationResponse.TopadsStatisticsEstimationAttribute.DataItem) -> Unit,
        shopId: String,
        source: String,
    ) {
        launchCatchError(block = {
            val data = withContext(dispatcher.io) {
                val request =
                    RequestHelper.getGraphQlRequest(rawQueries[RawQueryKeyObject.QUERY_POTENTIAL_REACH_ESTIMATION],
                        EstimationResponse::class.java,
                        hashMapOf(SHOP_ID to shopId, TYPE to 1, SOURCE to source))
                val cacheStrategy = RequestHelper.getCacheStrategy()
                repository.response(listOf(request), cacheStrategy)
            }
            onSuccess(data.getSuccessData<EstimationResponse>().topadsStatisticsEstimationAttribute.data[0])
        }) {
            it.printStackTrace()
        }
    }

    private fun getParams(dataParams: AutoAdsParam): RequestParams {
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
        const val SOURCE = "source"
        const val TYPE = "type"
    }
}

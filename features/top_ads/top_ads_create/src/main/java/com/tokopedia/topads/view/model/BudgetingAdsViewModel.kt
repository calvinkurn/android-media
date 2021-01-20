package com.tokopedia.topads.view.model

import android.content.Context
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.topads.common.data.internal.ParamObject.KEYWORD
import com.tokopedia.topads.common.data.internal.ParamObject.PRODUCT
import com.tokopedia.topads.common.data.internal.ParamObject.REQUEST_TYPE
import com.tokopedia.topads.common.data.internal.ParamObject.SHOP_Id
import com.tokopedia.topads.common.data.internal.ParamObject.SOURCE
import com.tokopedia.topads.common.data.internal.ParamObject.SOURCE_VALUE
import com.tokopedia.topads.common.data.internal.ParamObject.SUGGESTION
import com.tokopedia.topads.common.data.model.DataSuggestions
import com.tokopedia.topads.create.R
import com.tokopedia.topads.data.response.BidInfoDataItem
import com.tokopedia.topads.data.response.ResponseBidInfo
import com.tokopedia.topads.view.RequestHelper
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Author errysuprayogi on 06,November,2019
 */
class BudgetingAdsViewModel @Inject constructor(
        private val context: Context,
        private val userSession: UserSessionInterface,
        private val dispatcher: CoroutineDispatchers,
        private val repository: GraphqlRepository) : BaseViewModel(dispatcher.main) {


    fun getBidInfo(suggestions: List<DataSuggestions>, onSuccess: (List<BidInfoDataItem>) -> Unit, onEmpty: (() -> Unit)) {

        launchCatchError(
                block = {
                    val data = withContext(dispatcher.io) {
                        val request = RequestHelper.getGraphQlRequest(GraphqlHelper.loadRawString(context.resources, R.raw.query_ads_create_bid_info),
                                ResponseBidInfo.Result::class.java, getQueryMap(suggestions, KEYWORD))
                        val cacheStrategy = RequestHelper.getCacheStrategy()
                        repository.getReseponse(listOf(request), cacheStrategy)
                    }
                    data.getSuccessData<ResponseBidInfo.Result>().let {
                        if (it.topadsBidInfo.data.isEmpty()) {
                            onEmpty()
                        } else {
                            onSuccess(it.topadsBidInfo.data)
                        }
                    }

                },
                onError = {
                    it.printStackTrace()
                }
        )
    }

    private fun getQueryMap(suggestions: List<DataSuggestions>, requestType: String): HashMap<String, Any> {
        val queryMap = HashMap<String, Any>()
        queryMap[SHOP_Id] = Integer.parseInt(userSession.shopId)
        queryMap[SOURCE] = SOURCE_VALUE
        queryMap[SUGGESTION] = suggestions
        queryMap[REQUEST_TYPE] = requestType
        return queryMap
    }

    fun getBidInfoDefault(suggestions: List<DataSuggestions>, onSuccess: (List<BidInfoDataItem>) -> Unit) {

        launchCatchError(
                block = {
                    val data = withContext(dispatcher.io) {
                        val request = RequestHelper.getGraphQlRequest(GraphqlHelper.loadRawString(context.resources, R.raw.query_ads_create_bid_info),
                                ResponseBidInfo.Result::class.java, getQueryMap(suggestions, PRODUCT))
                        val cacheStrategy = RequestHelper.getCacheStrategy()
                        repository.getReseponse(listOf(request), cacheStrategy)
                    }
                    data.getSuccessData<ResponseBidInfo.Result>().let {
                        onSuccess(it.topadsBidInfo.data)
                    }

                },
                onError = {
                    it.printStackTrace()
                })
    }
}
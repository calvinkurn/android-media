package com.tokopedia.topads.view.model

import android.content.Context
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.topads.create.R
import com.tokopedia.topads.data.response.DataSuggestions
import com.tokopedia.topads.data.response.ResponseBidInfo
import com.tokopedia.topads.common.data.internal.ParamObject.KEYWORD
import com.tokopedia.topads.common.data.internal.ParamObject.PRODUCT
import com.tokopedia.topads.common.data.internal.ParamObject.REQUEST_TYPE
import com.tokopedia.topads.common.data.internal.ParamObject.SHOP_Id
import com.tokopedia.topads.common.data.internal.ParamObject.SOURCE
import com.tokopedia.topads.common.data.internal.ParamObject.SOURCE_VALUE
import com.tokopedia.topads.common.data.internal.ParamObject.SUGGESTION
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

/**
 * Author errysuprayogi on 06,November,2019
 */
class BudgetingAdsViewModel @Inject constructor(
        private val context: Context,
        private val userSession: UserSessionInterface,
        @Named("Main")
        private val dispatcher: CoroutineDispatcher,
        private val repository: GraphqlRepository) : BaseViewModel(dispatcher) {


    fun getBidInfo(suggestions: List<DataSuggestions>, onSuccess: (List<ResponseBidInfo.Result.TopadsBidInfo.DataItem>) -> Unit, onError: ((Throwable) -> Unit), onEmpty: (() -> Unit)) {

        launchCatchError(
                block = {
                    val queryMap = HashMap<String, Any?>()
                    queryMap[SHOP_Id] = Integer.parseInt(userSession.shopId)
                    queryMap[SOURCE] = SOURCE_VALUE
                    queryMap[SUGGESTION] = suggestions
                    queryMap[REQUEST_TYPE] = KEYWORD
                    val data = withContext(Dispatchers.IO) {
                        val request = GraphqlRequest(GraphqlHelper.loadRawString(context.resources, R.raw.query_ads_create_bid_info), ResponseBidInfo.Result::class.java, queryMap)
                        val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.CLOUD_THEN_CACHE).build()
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
                    onError(it)
                }
        )
    }

    fun getBidInfoDefault(suggestions: List<DataSuggestions>, onSuccess: (List<ResponseBidInfo.Result.TopadsBidInfo.DataItem>) -> Unit, onError: ((Throwable) -> Unit), onEmpty: (() -> Unit)) {

        launchCatchError(
                block = {
                    val queryMap = HashMap<String, Any?>()
                    queryMap[SHOP_Id] = Integer.parseInt(userSession.shopId)
                    queryMap[SOURCE] = SOURCE_VALUE
                    queryMap[SUGGESTION] = suggestions
                    queryMap[REQUEST_TYPE] = PRODUCT
                    val data = withContext(Dispatchers.IO) {
                        val request = GraphqlRequest(GraphqlHelper.loadRawString(context.resources, R.raw.query_ads_create_bid_info), ResponseBidInfo.Result::class.java, queryMap)
                        val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.CLOUD_THEN_CACHE).build()
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
                    onError(it)
                })
    }
}
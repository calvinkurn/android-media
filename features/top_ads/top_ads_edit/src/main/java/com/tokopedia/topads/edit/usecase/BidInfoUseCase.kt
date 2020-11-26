package com.tokopedia.topads.edit.usecase

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.di.ActivityContext
import com.tokopedia.topads.edit.R
import com.tokopedia.topads.edit.data.param.DataSuggestions
import com.tokopedia.topads.edit.data.response.ResponseBidInfo
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by Pika on 24/5/20.
 */

class BidInfoUseCase @Inject constructor(@ActivityContext val context: Context?, graphqlRepository: GraphqlRepository, val userSession: UserSessionInterface) : GraphqlUseCase<ResponseBidInfo.Result>(graphqlRepository) {


    fun setParams(suggestion: List<DataSuggestions>, requestType: String) {
        val queryMap = HashMap<String, Any?>()
        queryMap[ParamObject.SHOP_Id] = userSession.shopId.toInt()
        queryMap[ParamObject.SOURCE] = ParamObject.SOURCE_VALUE
        queryMap[ParamObject.SUGGESTION] = suggestion
        queryMap[ParamObject.REQUEST_TYPE] = requestType
        setRequestParams(queryMap)
    }

    private fun getQuery(): String {
        return GraphqlHelper.loadRawString(context?.resources, R.raw.query_ads_bid_info)
    }

    private val cacheStrategy: GraphqlCacheStrategy = GraphqlCacheStrategy
            .Builder(CacheType.CLOUD_THEN_CACHE).build()

    fun executeQuerySafeMode(onSuccess: (ResponseBidInfo.Result) -> Unit, onError: (Throwable) -> Unit) {
        setTypeClass(ResponseBidInfo.Result::class.java)
        setGraphqlQuery(getQuery())
        setCacheStrategy(cacheStrategy)
        execute({
            onSuccess(it)

        }, onError)
    }
}
package com.tokopedia.topads.common.domain.interactor

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.internal.ParamObject.SOURCE_VALUE
import com.tokopedia.topads.common.data.model.DataSuggestions
import com.tokopedia.topads.common.data.response.ResponseBidInfo
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by Pika on 9/10/20.
 */

private const val BID_INFO: String = """
query BidInfo(
  ${'$'}dataSuggestions: [BidInfoDataSuggestions]!,
  ${'$'}shopId: Int!,
  ${'$'}requestType: String!,
  ${'$'}source: String!
) {
  topadsBidInfo(dataSuggestions: ${'$'}dataSuggestions, shopId: ${'$'}shopId, requestType: ${'$'}requestType, source: ${'$'}source) {
    data {
      id
      max_bid
      max_bid_fmt
      min_bid
      min_bid_fmt
      min_daily_budget
      max_daily_budget
      min_daily_budget_fmt
      max_daily_budget_fmt
      multiplier
      suggestion_bid
      suggestion_bid_fmt
      estimation {
        min_bid
        min_bid_fmt
        max_bid
        max_bid_fmt
      }
    }
    request_type
  }
}"""

@GqlQuery("GetBidInfoQuery", BID_INFO)

class BidInfoUseCase @Inject constructor(graphqlRepository: GraphqlRepository, val userSession: UserSessionInterface) : GraphqlUseCase<ResponseBidInfo.Result>(graphqlRepository) {


    fun setParams(suggestion: List<DataSuggestions>, requestType: String, sourceValue: String = SOURCE_VALUE) {
        val queryMap = HashMap<String, Any?>()
        queryMap[ParamObject.SHOP_Id] = userSession.shopId.toIntOrZero()
        queryMap[ParamObject.SOURCE] = sourceValue
        queryMap[ParamObject.SUGGESTION] = suggestion
        queryMap[ParamObject.REQUEST_TYPE] = requestType
        setRequestParams(queryMap)
    }

    private fun getQuery(): String {
        return GetBidInfoQuery.GQL_QUERY
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
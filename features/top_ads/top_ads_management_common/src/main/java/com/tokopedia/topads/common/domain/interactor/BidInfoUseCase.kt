package com.tokopedia.topads.common.domain.interactor

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.model.DataSuggestions
import com.tokopedia.topads.common.data.response.ResponseBidInfo
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by Pika on 9/10/20.
 */

private const val BID_INFO :String = """
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
      min_bid
      suggestion_bid
    }
    request_type
  }
}"""

@GqlQuery("GetBidInfoQuery", BID_INFO)

class BidInfoUseCase @Inject constructor(graphqlRepository: GraphqlRepository, val userSession: UserSessionInterface) : GraphqlUseCase<ResponseBidInfo.Result>(graphqlRepository) {


    fun setParams(suggestion: List<DataSuggestions>, requestType: String) {
        val queryMap = HashMap<String, Any?>()
        queryMap[ParamObject.SHOP_Id] = userSession.shopId.toInt()
        queryMap[ParamObject.SOURCE] = ParamObject.SOURCE_VALUE
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
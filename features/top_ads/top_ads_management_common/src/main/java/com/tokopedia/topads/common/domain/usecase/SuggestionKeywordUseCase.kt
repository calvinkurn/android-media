package com.tokopedia.topads.common.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.response.KeywordSuggestionResponse
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by Pika on 6/11/20.
 */
private const val KEYWORD_SUGGESTION: String = """
               query topAdsGetKeywordSuggestionV3(${'$'}product_ids : String!,${'$'}group_id : Int,${'$'}shop_id : Int!,${'$'}type : Int) {
                 topAdsGetKeywordSuggestionV3(product_ids : ${'$'}product_ids, group_id : ${'$'}group_id, shop_id  : ${'$'}shop_id, type : ${'$'}type) {
                   data {
                     min_bid
                     product_id
                     keyword_data {
                       keyword
                       bid_suggest
                       total_search
                       source
                       competition
                     }
                   }
                   errors {
                     code
                     detail
                     title
                   }
                 }
               }
           """


@GqlQuery("TopAdsGetKeywordsQuery", KEYWORD_SUGGESTION)
class SuggestionKeywordUseCase @Inject constructor(graphqlRepository: GraphqlRepository, val userSession: UserSessionInterface) : GraphqlUseCase<KeywordSuggestionResponse.Result>(graphqlRepository) {

    fun setParams(groupId: Int?, productIds: String?,type:Int = 1) {
        val queryMap = HashMap<String, Any?>()
        queryMap[ParamObject.PRODUCT_IDS] = productIds
        queryMap[ParamObject.GROUP_ID] = groupId
        queryMap[ParamObject.SHOP_id] = userSession.shopId.toIntOrZero()
        queryMap[ParamObject.TYPE] = type
        setRequestParams(queryMap)
    }

    private fun getQuery(): String {
        return TopAdsGetKeywordsQuery.GQL_QUERY
    }

    private val cacheStrategy: GraphqlCacheStrategy = GraphqlCacheStrategy
            .Builder(CacheType.CLOUD_THEN_CACHE).build()

    fun executeQuerySafeMode(onSuccess: (KeywordSuggestionResponse.Result) -> Unit, onError: (Throwable) -> Unit) {
        setTypeClass(KeywordSuggestionResponse.Result::class.java)
        setGraphqlQuery(getQuery())
        setCacheStrategy(cacheStrategy)
        execute({
            onSuccess(it)

        }, onError)
    }
}
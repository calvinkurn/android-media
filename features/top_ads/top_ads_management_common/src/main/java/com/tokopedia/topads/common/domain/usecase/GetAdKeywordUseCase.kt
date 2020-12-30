package com.tokopedia.topads.common.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.internal.ParamObject.LIMIT
import com.tokopedia.topads.common.data.internal.ParamObject.NEXT_CURSOR
import com.tokopedia.topads.common.data.internal.ParamObject.PAGE
import com.tokopedia.topads.common.data.response.GetKeywordResponse
import javax.inject.Inject

/**
 * Created by Pika on 24/5/20.
 */


const val GET_AD_KEYWORD_QUERY = """query topAdsListKeyword(${'$'}source: String!, ${'$'}filter: TopAdsKeywordFilterReq, ${'$'}page: TopAdsKeywordPageReq) {
  topAdsListKeyword(source: ${'$'}source, filter: ${'$'}filter, page: ${'$'}page) {
    error {
      code
      title
      detail
    }
    data {
      keywords {
        keyword_id
		    shop_id
		    group_id
		    tag
		    type
		    status
		    price_bid
		    create_by
		    create_time_utc
		    update_by
		    update_time_utc
      }
      pagination {
        next_cursor
      }
    }
  }
}
"""

@GqlQuery("GetAdKeywordQuery", GET_AD_KEYWORD_QUERY)
class GetAdKeywordUseCase @Inject constructor(graphqlRepository: GraphqlRepository) : GraphqlUseCase<GetKeywordResponse>(graphqlRepository) {

    fun setParams(groupId: Int, cursor: String, shopId: String, source:String, limit:Int = 50, keywordStatus:List<Int> = emptyList()) {
        val map = HashMap<String, Any?>()
        map[ParamObject.SHOP_id] = shopId
        map[ParamObject.GROUP_ID] = groupId.toString()
        map[ParamObject.KEYWORD_STATUS] = keywordStatus
        val page = HashMap<String, Any?>()
        page[NEXT_CURSOR] = cursor
        page[LIMIT] = limit
        val queryMap = mapOf(ParamObject.SOURCE to source, ParamObject.FILTER to map, PAGE to page)
        setRequestParams(queryMap)
    }

    private fun getQuery(): String {
        return GetAdKeywordQuery.GQL_QUERY
    }

    private val cacheStrategy: GraphqlCacheStrategy = GraphqlCacheStrategy
            .Builder(CacheType.CLOUD_THEN_CACHE).build()

    fun executeQuerySafeMode(onSuccess: (GetKeywordResponse) -> Unit, onError: (Throwable) -> Unit) {
        setTypeClass(GetKeywordResponse::class.java)
        setGraphqlQuery(getQuery())
        setCacheStrategy(cacheStrategy)
        execute({
            onSuccess(it)
        }, onError)
    }
}
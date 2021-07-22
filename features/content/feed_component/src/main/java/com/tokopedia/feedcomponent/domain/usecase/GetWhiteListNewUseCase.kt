package com.tokopedia.feedcomponent.domain.usecase

import com.tokopedia.feedcomponent.data.pojo.whitelist.WhitelistQuery
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import java.util.*
import javax.inject.Inject

/**
 * @author by yfsx on 20/06/18.
 */

const val GET_WHITE_LIST_QUERY: String = """query WhitelistQuery(${'$'}type: String, ${'$'}ID: String) {
  feed_check_whitelist(type: ${'$'}type, ID: ${'$'}ID) {
    __typename
    iswhitelist
    url
    title
    title_identifier
    description
    post_success
    image_url
    authors {
      id
      name
      title
      thumbnail
      link
      badge
      type
    }
    error
  }
}
"""

private const val PARAM_TYPE = "type"
private const val PARAM_ID = "ID"
const val WHITELIST_INTEREST = "interest"

@GqlQuery("GetWhitelistQuery", GET_WHITE_LIST_QUERY)
class GetWhitelistNewUseCase @Inject constructor(graphqlRepository: GraphqlRepository)
    : GraphqlUseCase<WhitelistQuery>(graphqlRepository) {

    init {
        setTypeClass(WhitelistQuery::class.java)
        setCacheStrategy(
                GraphqlCacheStrategy.Builder(CacheType.CLOUD_THEN_CACHE)
                        .setExpiryTime(GraphqlConstant.ExpiryTimes.WEEK.`val`())
                        .setSessionIncluded(true)
                        .build())
        setGraphqlQuery(GetWhitelistQuery.GQL_QUERY)
    }

    fun createRequestParams(type: String, id: String = "") {
        val variables = HashMap<String, Any>()
        variables[PARAM_TYPE] = type
        variables[PARAM_ID] = id
        setRequestParams(variables)
    }

    suspend fun execute(type: String, id: String = "") : WhitelistQuery {
        this.createRequestParams(type, id)
        return executeOnBackground()
    }
}

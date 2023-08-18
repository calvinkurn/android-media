package com.tokopedia.topads.common.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.internal.ParamObject.GROUP_TYPE
import com.tokopedia.topads.common.data.internal.ParamObject.KEYWORD
import com.tokopedia.topads.common.data.internal.ParamObject.SINGLE_ROW
import com.tokopedia.topads.common.data.model.DashGroupListResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject
import kotlin.collections.set

/**
 * Created by Pika on 29/5/20.
 */
const val GROUP_LIST_QUERY = """
    query GetTopadsDashboardGroupsV3(${'$'}queryInput: GetTopadsDashboardGroupsInputTypeV3!) {
  GetTopadsDashboardGroupsV3(queryInput: ${'$'}queryInput) {
       meta {
          page {
            per_page
            current
            total
          }
        }
    data {
      group_id
      group_type
      total_item
      total_keyword
      group_status_desc
      group_name

    }
  }
}
"""

@GqlQuery("GetTopadsGroupListQuery", GROUP_LIST_QUERY)
class TopAdsGetGroupListUseCase @Inject constructor(val userSession: UserSessionInterface, graphqlRepository: GraphqlRepository) {

    private val graphql by lazy { GraphqlUseCase<DashGroupListResponse>(graphqlRepository) }

    suspend fun execute(requestParams: RequestParams): DashGroupListResponse {
        graphql.apply {
            setGraphqlQuery(GetTopadsGroupListQuery.GQL_QUERY)
            setTypeClass(DashGroupListResponse::class.java)
        }

        return graphql.run {
            setRequestParams(requestParams.parameters)
            executeOnBackground()
        }
    }

    fun setParamsForKeyWord(search: String): RequestParams {
        val requestParams = RequestParams.create()
        val queryMap = HashMap<String, Any?>()
        queryMap[ParamObject.SHOP_id] = userSession.shopId
        queryMap[ParamObject.SEPARATE_STAT] = "true"
        queryMap[KEYWORD] = search
        queryMap[GROUP_TYPE] = 1
        queryMap[SINGLE_ROW] = "1"/*keywords*/
        requestParams.putAll(mapOf(ParamObject.QUERY_INPUT to queryMap))
        return requestParams
    }
}

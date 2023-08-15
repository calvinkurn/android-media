package com.tokopedia.topads.dashboard.domain.interactor

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.internal.ParamObject.END_DATE
import com.tokopedia.topads.common.data.internal.ParamObject.GOAL_ID
import com.tokopedia.topads.common.data.internal.ParamObject.GROUP_iDS
import com.tokopedia.topads.common.data.internal.ParamObject.KEYWORD
import com.tokopedia.topads.common.data.internal.ParamObject.PAGE
import com.tokopedia.topads.common.data.internal.ParamObject.QUERY_INPUT
import com.tokopedia.topads.common.data.internal.ParamObject.SORT
import com.tokopedia.topads.common.data.internal.ParamObject.START_DATE
import com.tokopedia.topads.common.data.response.groupitem.GroupStatisticsResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject
import kotlin.collections.set

/**
 * Created by Pika on 29/5/20.
 */

private const val TOP_ADS_GET_GROUP_STATISTICS_QUERY: String =
    """query GetTopadsDashboardGroupStatisticsV3(${'$'}queryInput: GetTopadsDashboardGroupStatisticsInputTypeV3!) {
  GetTopadsDashboardGroupStatisticsV3(queryInput: ${'$'}queryInput) {
    separate_statistic
    meta {
      page {
        per_page
        current
      }
    }
    data {
      group_id
      stat_avg_click
      stat_total_spent
      stat_total_impression
      stat_total_click
      stat_total_ctr
      stat_total_conversion
      stat_total_sold
      stat_total_income
      group_price_daily_spent_fmt
      group_price_daily
    }
  }
}
"""

@GqlQuery("GetTopadsDashboardGroupStatisticsQuery", TOP_ADS_GET_GROUP_STATISTICS_QUERY)
class TopAdsGetGroupStatisticsUseCase @Inject constructor(
    private val userSession: UserSessionInterface,
    graphqlRepository: GraphqlRepository
) {

    private val graphql by lazy { GraphqlUseCase<GroupStatisticsResponse>(graphqlRepository) }

    suspend fun execute(requestParams: RequestParams): GroupStatisticsResponse {
        graphql.apply {
            setGraphqlQuery(GetTopadsDashboardGroupStatisticsQuery.GQL_QUERY)
            setTypeClass(GroupStatisticsResponse::class.java)
        }

        return graphql.run {
            setRequestParams(requestParams.parameters)
            executeOnBackground()
        }
    }

    fun setParams(
        search: String,
        page: Int,
        sort: String,
        status: Int?,
        startDate: String,
        endDate: String,
        groupids: List<String>,
        goalId: Int = 0
    ): RequestParams {
        val requestParams = RequestParams.create()
        val queryMap = HashMap<String, Any?>()
        queryMap[ParamObject.SHOP_id] = userSession.shopId
        queryMap[SORT] = sort
        queryMap[KEYWORD] = search
        queryMap[PAGE] = page
        queryMap[START_DATE] = startDate
        queryMap[END_DATE] = endDate
        queryMap[GOAL_ID] = goalId.toString()
        queryMap[GROUP_iDS] = groupids.joinToString(",")
        requestParams.putAll(mapOf(QUERY_INPUT to queryMap))
        return requestParams
    }
}

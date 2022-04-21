package com.tokopedia.topads.dashboard.domain.interactor

import com.google.gson.reflect.TypeToken
import com.tokopedia.common.network.coroutines.RestRequestInteractor
import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.topads.common.constant.TopAdsCommonConstant
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
    """query GetTopadsDashboardGroupStatisticsV2(${'$'}queryInput: GetTopadsDashboardGroupStatisticsInputTypeV2!) {
  GetTopadsDashboardGroupStatisticsV2(queryInput: ${'$'}queryInput) {
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
    }
  }
}
"""

@GqlQuery("GetTopadsDashboardGroupStatisticsQuery", TOP_ADS_GET_GROUP_STATISTICS_QUERY)
class TopAdsGetGroupStatisticsUseCase @Inject constructor(
    private val userSession: UserSessionInterface,
) {

    private val restRepository: RestRepository by lazy { RestRequestInteractor.getInstance().restRepository }

    suspend fun execute(requestParams: RequestParams?): GroupStatisticsResponse {
        try {
            val token = object : TypeToken<DataResponse<GroupStatisticsResponse>>() {}.type
            val query = GetTopadsDashboardGroupStatisticsQuery.GQL_QUERY
            val request = GraphqlRequest(
                query, GroupStatisticsResponse::class.java, requestParams?.parameters
            )
            val restRequest = RestRequest.Builder(TopAdsCommonConstant.TOPADS_GRAPHQL_TA_URL, token)
                .setBody(request)
                .setRequestType(RequestType.POST)
                .build()

            return restRepository.getResponse(restRequest)
                .getData<DataResponse<GroupStatisticsResponse>>().data
        } catch (e: Exception) {
            throw e
        }
    }

    fun setParams(
        search: String, page: Int, sort: String, status: Int?,
        startDate: String, endDate: String, groupids: List<String>, goalId: Int = 0,
    ): RequestParams {
        val requestParams = RequestParams.create()
        val queryMap = HashMap<String, Any?>()
        queryMap[ParamObject.SHOP_id] = userSession.shopId
        queryMap[SORT] = sort
        queryMap[KEYWORD] = search
        queryMap[PAGE] = page
        queryMap[START_DATE] = startDate
        queryMap[END_DATE] = endDate
        queryMap[GOAL_ID] = goalId
        queryMap[GROUP_iDS] = groupids.joinToString(",")
        requestParams.putAll(mapOf(QUERY_INPUT to queryMap))
        return requestParams
    }
}
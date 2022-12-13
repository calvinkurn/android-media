package com.tokopedia.topads.common.domain.interactor

import com.google.gson.reflect.TypeToken
import com.tokopedia.common.network.coroutines.RestRequestInteractor
import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.topads.common.constant.TopAdsCommonConstant
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.internal.ParamObject.END_DATE
import com.tokopedia.topads.common.data.internal.ParamObject.GROUP_TYPE
import com.tokopedia.topads.common.data.internal.ParamObject.KEYWORD
import com.tokopedia.topads.common.data.internal.ParamObject.PAGE
import com.tokopedia.topads.common.data.internal.ParamObject.QUERY_INPUT
import com.tokopedia.topads.common.data.internal.ParamObject.SORT
import com.tokopedia.topads.common.data.internal.ParamObject.START_DATE
import com.tokopedia.topads.common.data.internal.ParamObject.STATUS
import com.tokopedia.topads.common.data.response.groupitem.GroupItemResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject
import kotlin.collections.set

/**
 * Created by Pika on 6/9/20.
 */

const val TOP_ADS_GET_GROUP_LIST_QUERY: String =
    """query GetTopadsDashboardGroupsV3(${'$'}queryInput: GetTopadsDashboardGroupsInputTypeV3!) {
  GetTopadsDashboardGroupsV3(queryInput: ${'$'}queryInput) {
    separate_statistic
       meta {
          page {
            per_page
            current
            total
          }
        }
    data {
      group_id
      total_item
      total_keyword
      group_status
      group_status_desc
      group_status_toogle
      group_price_bid
      group_price_daily
      group_price_daily_spent_fmt
      group_price_daily_bar
      group_name
      group_type
      group_end_date
      stat_total_conversion
      stat_total_spent
      stat_total_ctr
      stat_total_sold
      stat_avg_click
      stat_total_income
      strategies
    }
  }
}
"""

@GqlQuery("GetTopadsGroupDataQuery", TOP_ADS_GET_GROUP_LIST_QUERY)
class TopAdsGetGroupDataUseCase @Inject constructor(private val userSession: UserSessionInterface) {

    private val restRepository: RestRepository by lazy { RestRequestInteractor.getInstance().restRepository }

    suspend fun execute(requestParams: RequestParams?): GroupItemResponse {
        val token = object : TypeToken<DataResponse<GroupItemResponse>>() {}.type
        val query = GetTopadsGroupDataQuery.GQL_QUERY
        val request =
            GraphqlRequest(query, GroupItemResponse::class.java, requestParams?.parameters)
        val headers = java.util.HashMap<String, String>()
        headers["Content-Type"] = "application/json"
        val restRequest = RestRequest.Builder(TopAdsCommonConstant.TOPADS_GRAPHQL_TA_URL, token)
            .setBody(request)
            .setHeaders(headers)
            .setRequestType(RequestType.POST)
            .build()
        return restRepository.getResponse(restRequest)
            .getData<DataResponse<GroupItemResponse>>().data
    }

    fun setParams(
        search: String, page: Int, sort: String, status: Int?,
        startDate: String, endDate: String, groupType: Int,
    ): RequestParams {
        val queryMap = HashMap<String, Any?>()
        val requestParams = RequestParams.create()
        queryMap[ParamObject.SHOP_id] = userSession.shopId
        queryMap[SORT] = sort
        queryMap[KEYWORD] = search
        queryMap[PAGE] = page
        queryMap[START_DATE] = startDate
        queryMap[END_DATE] = endDate
        queryMap[STATUS] = status
        queryMap[GROUP_TYPE] = groupType
        requestParams.putAll(mapOf(QUERY_INPUT to queryMap))
        return requestParams
    }
}

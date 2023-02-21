package com.tokopedia.topads.common.domain.interactor

import com.google.gson.reflect.TypeToken
import com.tokopedia.common.network.coroutines.RestRequestInteractor
import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.authentication.HEADER_CONTENT_TYPE
import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.topads.common.constant.TopAdsCommonConstant
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.internal.ParamObject.CONTENT_TYPE_JSON
import com.tokopedia.topads.common.data.internal.ParamObject.QUERY_INPUT
import com.tokopedia.topads.common.data.response.nongroupItem.NonGroupResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject
import kotlin.collections.set

/**
 * Created by Pika on 7/6/20.
 */

private const val TOP_ADS_DASHBOARD_GROUP_PRODUCTS_QUERY: String = """
               query topadsDashboardGroupProductsV4(${'$'}queryInput: topadsDashboardGroupProductsInputTypeV4!) {
  topadsDashboardGroupProductsV4(queryInput: ${'$'}queryInput) {
    separate_statistic
       meta {
              page {
                per_page
                current
                total
              }
            }
    data {
      ad_id
      item_id
      ad_status
      ad_status_desc
      ad_price_bid
      ad_price_bid_fmt
      ad_price_daily
      ad_price_daily_fmt
      stat_total_gross_profit
      ad_price_daily_spent_fmt
      ad_price_daily_bar
      product_name
      product_image_uri
      group_id
      group_name
    }
  }
}
"""

@GqlQuery("TopadsDashboardGroupProductsQuery", TOP_ADS_DASHBOARD_GROUP_PRODUCTS_QUERY)
class TopAdsGetGroupProductDataUseCase @Inject constructor(
    private val userSession: UserSessionInterface,
) {

    private val restRepository: RestRepository by lazy { RestRequestInteractor.getInstance().restRepository }

    suspend fun execute(requestParams: RequestParams?): NonGroupResponse {
        try {
            val token = object : TypeToken<DataResponse<NonGroupResponse>>() {}.type
            val query = TopadsDashboardGroupProductsQuery.GQL_QUERY
            val request =
                GraphqlRequest(query, NonGroupResponse::class.java, requestParams?.parameters)

            val restRequest = RestRequest.Builder(TopAdsCommonConstant.TOPADS_GRAPHQL_TA_URL, token)
                .setBody(request)
                .setHeaders(mapOf(HEADER_CONTENT_TYPE to CONTENT_TYPE_JSON))
                .setRequestType(RequestType.POST)
                .build()
            return restRepository.getResponse(restRequest)
                .getData<DataResponse<NonGroupResponse>>().data
        } catch (t: Throwable) {
            throw t
        }
    }

    fun setParams(
        groupId: String?, page: Int, search: String, sort: String, status: Int?,
        startDate: String, endDate: String, type: String = "", goalId: Int = 0,
    ): RequestParams {
        val requestParams = RequestParams.create()
        val queryMap = HashMap<String, Any?>()
        queryMap[ParamObject.SHOP_id] = userSession.shopId
        queryMap[ParamObject.GROUP] = groupId
        queryMap[ParamObject.SORT] = sort
        queryMap[ParamObject.PAGE] = page
        queryMap[ParamObject.START_DATE] = startDate
        queryMap[ParamObject.END_DATE] = endDate
        queryMap[ParamObject.KEYWORD] = search
        queryMap[ParamObject.STATUS] = status
        queryMap[ParamObject.TYPE] = type
        queryMap[ParamObject.GOAL_ID] = goalId.toString()
        requestParams.putAll(mapOf(QUERY_INPUT to queryMap))
        return requestParams
    }
}

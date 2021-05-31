package com.tokopedia.topads.common.domain.interactor

import com.google.gson.reflect.TypeToken
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.common.network.domain.RestRequestUseCase
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.topads.common.constant.TopAdsCommonConstant
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.internal.ParamObject.QUERY_INPUT
import com.tokopedia.topads.common.data.response.nongroupItem.NonGroupResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashMap
import kotlin.collections.set

/**
 * Created by Pika on 7/6/20.
 */

const val TOP_ADS_DASHBOARD_GROUP_PRODUCTS_QUERY: String = """
               query topadsDashboardGroupProducts(${'$'}queryInput: topadsDashboardGroupProductsInputType!) {
  topadsDashboardGroupProducts(queryInput: ${'$'}queryInput) {
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
class TopAdsGetGroupProductDataUseCase @Inject constructor(val userSession: UserSessionInterface) : RestRequestUseCase()  {


    override fun buildRequest(requestParams: RequestParams?): MutableList<RestRequest> {
        val tempRequest = ArrayList<RestRequest>()
        val token = object : TypeToken<DataResponse<NonGroupResponse>>() {}.type
        val query = TopadsDashboardGroupProductsQuery.GQL_QUERY
        val request = GraphqlRequest(query, NonGroupResponse::class.java, requestParams?.parameters)
        val headers = java.util.HashMap<String, String>()
        headers["Content-Type"] = "application/json"
        val restReferralRequest = RestRequest.Builder(TopAdsCommonConstant.TOPADS_GRAPHQL_TA_URL, token)
                .setBody(request)
                .setHeaders(headers)
                .setRequestType(RequestType.POST)
                .build()
        tempRequest.add(restReferralRequest)
        return tempRequest
    }

    fun setParams(groupId: Int?, page: Int,  search: String, sort: String, status: Int?, startDate: String, endDate: String, type: String = ""): RequestParams {

        val requestParams = RequestParams.create()
        val queryMap = HashMap<String, Any?>()
        queryMap[ParamObject.SHOP_id] = userSession.shopId.toIntOrZero()
        queryMap[ParamObject.GROUP] = groupId
        queryMap[ParamObject.SORT] = sort
        queryMap[ParamObject.PAGE] = page
        queryMap[ParamObject.START_DATE] = startDate
        queryMap[ParamObject.END_DATE] = endDate
        queryMap[ParamObject.KEYWORD] = search
        queryMap[ParamObject.STATUS] = status
        queryMap[ParamObject.TYPE] = type
        requestParams.putAll(mapOf(QUERY_INPUT to queryMap))
        return requestParams
    }
}
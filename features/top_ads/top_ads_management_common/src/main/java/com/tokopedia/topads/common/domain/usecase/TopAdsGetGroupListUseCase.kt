package com.tokopedia.topads.common.domain.usecase

import com.google.gson.reflect.TypeToken
import com.tokopedia.common.network.data.model.CacheType
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.common.network.data.model.RestCacheStrategy
import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.common.network.domain.RestRequestUseCase
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.topads.common.constant.TopAdsCommonConstant
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.internal.ParamObject.GROUP_TYPE
import com.tokopedia.topads.common.data.internal.ParamObject.KEYWORD
import com.tokopedia.topads.common.data.internal.ParamObject.SINGLE_ROW
import com.tokopedia.topads.common.data.model.DashGroupListResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashMap
import kotlin.collections.set

/**
 * Created by Pika on 29/5/20.
 */
const val GROUP_LIST_QUERY = """
    query GetTopadsDashboardGroups(${'$'}queryInput: GetTopadsDashboardGroupsInputType!) {
  GetTopadsDashboardGroups(queryInput: ${'$'}queryInput) {
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
      group_status_desc
      group_name

    }
  }
}
"""

@GqlQuery("GetTopadsGroupListQuery", GROUP_LIST_QUERY)
class TopAdsGetGroupListUseCase @Inject constructor(val userSession: UserSessionInterface) : RestRequestUseCase() {


    fun setParamsForKeyWord(search: String): RequestParams {
        val requestParams = RequestParams.create()
        val queryMap = HashMap<String, Any?>()
        queryMap[ParamObject.SHOP_id] = userSession.shopId.toIntOrZero()
        queryMap[ParamObject.SEPARATE_STAT] = "true"
        queryMap[KEYWORD] = search
        queryMap[GROUP_TYPE] = 1
        queryMap[SINGLE_ROW] = "1"/*keywords*/
        requestParams.putAll(mapOf(ParamObject.QUERY_INPUT to queryMap))
        return requestParams
    }

    override fun buildRequest(requestParams: RequestParams?): MutableList<RestRequest> {
        val tempRequest = ArrayList<RestRequest>()
        val token = object : TypeToken<DataResponse<DashGroupListResponse>>() {}.type
        val query = GetTopadsGroupListQuery.GQL_QUERY
        val request = GraphqlRequest(query, DashGroupListResponse::class.java, requestParams?.parameters)
        val headers = java.util.HashMap<String, String>()
        headers["Content-Type"] = "application/json"
        val restReferralRequest = RestRequest.Builder(TopAdsCommonConstant.TOPADS_GRAPHQL_TA_URL, token)
                .setBody(request)
                .setHeaders(headers)
                .setCacheStrategy(cacheStrategy)
                .setRequestType(RequestType.POST)
                .build()
        tempRequest.add(restReferralRequest)
        return tempRequest
    }

    private val cacheStrategy: RestCacheStrategy = RestCacheStrategy
            .Builder(CacheType.ALWAYS_CLOUD).build()
}
package com.tokopedia.topads.dashboard.domain.interactor

import com.google.gson.reflect.TypeToken
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.common.network.domain.RestRequestUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.topads.common.constant.TopAdsCommonConstant
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.internal.ParamObject.GROUPID
import com.tokopedia.topads.common.data.internal.ParamObject.PRICE_BID
import com.tokopedia.topads.common.data.internal.ParamObject.PRICE_DAILY
import com.tokopedia.topads.common.data.internal.ParamObject.SOURCE
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.ACTION
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.GROUPS
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant.SOURCE_DASH
import com.tokopedia.topads.dashboard.data.model.GroupActionResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by Pika on 29/5/20.
 */


class TopAdsGroupActionUseCase @Inject constructor(val userSession: UserSessionInterface) : RestRequestUseCase() {

    private var query = ""

    fun setQuery(queryString: String) {
        query = queryString
    }

    fun setParams(action: String, groupIds: List<String>): RequestParams {

        var requestParams = RequestParams.create()

        val group: ArrayList<Map<String, String?>> = arrayListOf()
        groupIds.forEach {
            val map = mapOf(GROUPID to it, PRICE_BID to null, PRICE_DAILY to null)
            group.add(map)
        }
        val queryMap = HashMap<String, Any?>()
        queryMap[ParamObject.SHOP_ID] = userSession.shopId
        queryMap[ACTION] = action
        queryMap[SOURCE] = SOURCE_DASH
        queryMap[GROUPS] = group
        requestParams.putAll(queryMap)
        return requestParams
    }

    override fun buildRequest(requestParams: RequestParams?): MutableList<RestRequest> {
        val tempRequest = java.util.ArrayList<RestRequest>()
        val token = object : TypeToken<DataResponse<GroupActionResponse>>() {}.type

        var request: GraphqlRequest = GraphqlRequest(query, GroupActionResponse::class.java, requestParams?.parameters)
        val restReferralRequest = RestRequest.Builder(TopAdsCommonConstant.TOPADS_GRAPHQL_TA_URL, token)
                .setBody(request)
                .setRequestType(RequestType.POST)
                .build()
        tempRequest.add(restReferralRequest)
        return tempRequest
    }
}
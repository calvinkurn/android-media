package com.tokopedia.topads.dashboard.domain.interactor

import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.common.network.domain.RestRequestUseCase
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.topads.common.constant.TopAdsCommonConstant
import com.tokopedia.topads.common.data.internal.ParamObject.INSIGHT_TYPE
import com.tokopedia.topads.common.data.internal.ParamObject.SHOP_Id
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import java.util.*
import javax.inject.Inject


/**
 * Created by Pika on 13/7/20.
 */

class TopAdsInsightUseCase @Inject constructor(val userSession: UserSessionInterface) : RestRequestUseCase() {

    private var query = ""

    fun setQuery(query: String) {
        this.query = query
    }


    override fun buildRequest(requestParams: RequestParams?): MutableList<RestRequest> {
        val tempRequest = ArrayList<RestRequest>()
        val token = object : TypeToken<DataResponse<JsonObject>>() {}.type

        var request: GraphqlRequest = GraphqlRequest(query, JsonObject::class.java, requestParams?.parameters)
        val restReferralRequest = RestRequest.Builder(TopAdsCommonConstant.TOPADS_GRAPHQL_TA_URL, token)
                .setBody(request)
                .setRequestType(RequestType.POST)
                .build()
        tempRequest.add(restReferralRequest)
        return tempRequest
    }
    fun setParams() : RequestParams{
        val requestParams = RequestParams.create()
        requestParams.putAll(mapOf(SHOP_Id to userSession.shopId.toInt(), INSIGHT_TYPE to "all"))
        return requestParams
    }
}
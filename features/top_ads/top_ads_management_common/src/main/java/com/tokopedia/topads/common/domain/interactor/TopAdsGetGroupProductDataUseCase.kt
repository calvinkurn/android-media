package com.tokopedia.topads.common.domain.interactor

import com.google.gson.reflect.TypeToken
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.common.network.domain.RestRequestUseCase
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.view.toUrlParams
import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.internal.ParamObject.QUERY_INPUT
import com.tokopedia.topads.common.data.response.nongroupItem.NonGroupResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import java.util.ArrayList
import javax.inject.Inject

/**
 * Created by Pika on 7/6/20.
 */

class TopAdsGetGroupProductDataUseCase @Inject constructor(val userSession: UserSessionInterface) : RestRequestUseCase()  {


    var query = ""

    fun setQueryString(queryString: String) {
        query = queryString
    }

    override fun buildRequest(requestParams: RequestParams?): MutableList<RestRequest> {
        val tempRequest = ArrayList<RestRequest>()
        val url = "https://gql.tokopedia.com/graphql/ta"
        val token = object : TypeToken<DataResponse<NonGroupResponse>>() {}.type

        var request: GraphqlRequest = GraphqlRequest(query, NonGroupResponse::class.java, requestParams?.parameters)
        val headers = java.util.HashMap<String, String>()
        headers.put("Content-Type", "application/json")
        val restReferralRequest = RestRequest.Builder(url, token)
                .setBody(request)
                .setHeaders(headers)
                .setRequestType(RequestType.POST)
                .build()
        tempRequest.add(restReferralRequest)
        return tempRequest
    }

    fun setParams(groupId: Int?, page: Int,  search: String, sort: String, status: Int?, startDate: String, endDate: String): RequestParams {

        val requestParams = RequestParams.create()
        val queryMap = HashMap<String, Any?>()
        queryMap[ParamObject.SHOP_id] = userSession.shopId.toInt()
        queryMap[ParamObject.GROUP] = groupId
        queryMap[ParamObject.SORT] = sort
        queryMap[ParamObject.PAGE] = page
        queryMap[ParamObject.START_DATE] = startDate
        queryMap[ParamObject.END_DATE] = endDate
        queryMap[ParamObject.KEYWORD] = search
        queryMap[ParamObject.STATUS] = status
        requestParams.putAll(mapOf(QUERY_INPUT to queryMap))
        return requestParams
    }
}
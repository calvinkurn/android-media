package com.tokopedia.topads.common.domain.interactor

import com.google.gson.reflect.TypeToken
import com.tokopedia.common.network.data.model.CacheType
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.common.network.data.model.RestCacheStrategy
import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.common.network.domain.RestRequestUseCase
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.topads.common.constant.TopAdsCommonConstant
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.internal.ParamObject.ACTION
import com.tokopedia.topads.common.data.internal.ParamObject.ADS
import com.tokopedia.topads.common.data.internal.ParamObject.PRICE_BID
import com.tokopedia.topads.common.data.response.ProductActionResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by Pika on 5/6/20.
 */

const val UPDATE_SINGLE_ADS = """
    mutation topadsUpdateSingleAds(${'$'}action: String!, ${'$'}ads: [topadsUpdateSingleAdsReqData]!, ${'$'}shopID: String!) {
  topadsUpdateSingleAds(action: ${'$'}action, ads: ${'$'}ads, shopID: ${'$'}shopID) {
        data {
            action
            shopID
        }
        errors {
            code
            detail
            title
        }
    }
}
"""
@GqlQuery("GetTopadsUpdateSingleAds", UPDATE_SINGLE_ADS)
class TopAdsProductActionUseCase @Inject constructor(val userSession: UserSessionInterface) : RestRequestUseCase() {

    fun setParams(action: String, adIds: List<String>, selectedFilter: String?): RequestParams {

        val params = RequestParams.create()
        val product: ArrayList<Map<String, String?>> = arrayListOf()

        adIds.forEach {
            val map = mapOf(ParamObject.AD_ID to it, ParamObject.GROUPID to (selectedFilter
                    ?: ""), PRICE_BID to null)
            product.add(map)
        }
        val queryMap = HashMap<String, Any?>()
        queryMap[ParamObject.SHOP_ID] = userSession.shopId
        queryMap[ACTION] = action
        queryMap[ADS] = product
        params.putAll(queryMap)
        return params
    }

    private val cacheStrategy: RestCacheStrategy = RestCacheStrategy
            .Builder(CacheType.ALWAYS_CLOUD).build()


    override fun buildRequest(requestParams: RequestParams?): MutableList<RestRequest> {
        val tempRequest = java.util.ArrayList<RestRequest>()
        val token = object : TypeToken<DataResponse<ProductActionResponse>>() {}.type
        val query = GetTopadsUpdateSingleAds.GQL_QUERY
        val request = GraphqlRequest(query, ProductActionResponse::class.java, requestParams?.parameters)
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
}
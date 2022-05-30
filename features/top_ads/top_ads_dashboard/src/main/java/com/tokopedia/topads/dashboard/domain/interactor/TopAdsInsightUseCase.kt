package com.tokopedia.topads.dashboard.domain.interactor

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.tokopedia.common.network.coroutines.RestRequestInteractor
import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.topads.common.constant.TopAdsCommonConstant
import com.tokopedia.topads.common.data.internal.ParamObject.INSIGHT_TYPE
import com.tokopedia.topads.common.data.internal.ParamObject.SHOP_Id
import com.tokopedia.topads.dashboard.data.constant.TopAdsDashboardConstant
import com.tokopedia.topads.dashboard.data.model.insightkey.InsightKeyData
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject


/**
 * Created by Pika on 13/7/20.
 */

class TopAdsInsightUseCase @Inject constructor(val userSession: UserSessionInterface) {

    private val restRepository: RestRepository by lazy { RestRequestInteractor.getInstance().restRepository }

    suspend fun execute(query: String, requestParams: RequestParams?): InsightKeyData {
        try {
            val token = object : TypeToken<DataResponse<JsonObject>>() {}.type
            val request = GraphqlRequest(query, JsonObject::class.java, requestParams?.parameters)

            val restRequest = RestRequest.Builder(TopAdsCommonConstant.TOPADS_GRAPHQL_TA_URL, token)
                .setBody(request)
                .setRequestType(RequestType.POST)
                .build()

            val response = restRepository.getResponse(restRequest)
                .getData<DataResponse<JsonObject>>().data.getAsJsonObject("topAdsGetKeywordInsights")
                .getAsJsonPrimitive(TopAdsDashboardConstant.DATA)
            return Gson().fromJson(response.asString, object : TypeToken<InsightKeyData>() {}.type)
        } catch (e: Exception) {
            throw e
        }
    }

    fun setParams(): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putAll(mapOf(SHOP_Id to userSession.shopId.toInt(), INSIGHT_TYPE to "all"))
        return requestParams
    }
}
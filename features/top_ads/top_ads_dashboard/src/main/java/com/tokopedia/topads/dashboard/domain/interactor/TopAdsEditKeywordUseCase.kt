package com.tokopedia.topads.dashboard.domain.interactor

import com.google.gson.reflect.TypeToken
import com.tokopedia.common.network.coroutines.RestRequestInteractor
import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.data.model.response.DataResponse
import com.tokopedia.topads.common.constant.TopAdsCommonConstant
import com.tokopedia.topads.common.data.internal.ParamObject.DATA
import com.tokopedia.topads.common.data.internal.ParamObject.GROUP_ID
import com.tokopedia.topads.common.data.internal.ParamObject.INSIGHT_SOURCE
import com.tokopedia.topads.common.data.internal.ParamObject.SHOP_id
import com.tokopedia.topads.common.data.internal.ParamObject.SOURCE
import com.tokopedia.topads.common.data.response.FinalAdResponse
import com.tokopedia.topads.dashboard.data.model.insightkey.MutationData
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import java.util.*
import javax.inject.Inject

/**
 * Created by Pika on 22/7/20.
 */

class TopAdsEditKeywordUseCase @Inject constructor(val userSession: UserSessionInterface) {

    private val restRepository: RestRepository by lazy { RestRequestInteractor.getInstance().restRepository }

    suspend fun execute(query: String, requestParams: RequestParams?): FinalAdResponse {
        try {
            val token = object : TypeToken<DataResponse<FinalAdResponse>>() {}.type
            val request =
                GraphqlRequest(query, FinalAdResponse::class.java, requestParams?.parameters)
            val restRequest = RestRequest.Builder(TopAdsCommonConstant.TOPADS_GRAPHQL_TA_URL, token)
                .setBody(request)
                .setRequestType(RequestType.POST)
                .build()
            return restRepository.getResponse(restRequest)
                .getData<DataResponse<FinalAdResponse>>().data
        } catch (e: Exception) {
            throw e
        }
    }

    fun setParam(groupId: String, data: List<MutationData>): RequestParams {
        val variable: HashMap<String, Any> = HashMap()
        val requestParams = RequestParams.create()
        variable[SHOP_id] = userSession.shopId.toString()
        variable[GROUP_ID] = groupId
        variable[SOURCE] = INSIGHT_SOURCE
        variable[DATA] = data
        requestParams.putAll(variable)
        return requestParams
    }
}
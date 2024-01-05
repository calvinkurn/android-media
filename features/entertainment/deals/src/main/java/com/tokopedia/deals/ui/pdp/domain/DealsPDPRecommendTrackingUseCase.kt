package com.tokopedia.deals.ui.pdp.domain

import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.common.network.coroutines.usecase.RestRequestUseCase
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.url.TokopediaUrl
import java.lang.reflect.Type
import javax.inject.Inject

class DealsPDPRecommendTrackingUseCase @Inject constructor(private val repository: RestRepository) : RestRequestUseCase(repository) {
    private var url: String = TokopediaUrl.getInstance().BOOKING + DEALS_TRACKING
    private var dealsRecommendTrackingRequest: com.tokopedia.deals.ui.pdp.data.DealsRecommendTrackingRequest =
        com.tokopedia.deals.ui.pdp.data.DealsRecommendTrackingRequest()

    fun setParam(dealsRecommendTrackingRequestParam: com.tokopedia.deals.ui.pdp.data.DealsRecommendTrackingRequest) {
        dealsRecommendTrackingRequest = dealsRecommendTrackingRequestParam
    }

    override suspend fun executeOnBackground(): Map<Type, RestResponse?> {
        val restRequest = RestRequest.Builder(url, com.tokopedia.deals.ui.pdp.data.DealsTrackingResponse::class.java)
            .setBody(dealsRecommendTrackingRequest)
            .setRequestType(RequestType.POST)
            .build()
        restRequestList.clear()
        restRequestList.add(restRequest)
        return repository.getResponses(restRequestList)
    }

    companion object {
        var DEALS_TRACKING = "/v1/api/tracking"
    }
}

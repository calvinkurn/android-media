package com.tokopedia.deals.pdp.domain

import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.common.network.coroutines.usecase.RestRequestUseCase
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.deals.pdp.data.DealsTrackingResponse
import com.tokopedia.deals.pdp.data.DealsTravelRecentSearchTrackingRequest
import com.tokopedia.url.TokopediaUrl
import java.lang.reflect.Type
import javax.inject.Inject

class DealsPDPRecentSearchTrackingUseCase @Inject constructor(private val repository: RestRepository) : RestRequestUseCase(repository) {

    private var url: String = TokopediaUrl.getInstance().BOOKING + DEALS_TRACKING
    private var dealsTravelRecentSearchTrackingRequest: DealsTravelRecentSearchTrackingRequest =
        DealsTravelRecentSearchTrackingRequest()

    fun setParam(dealsTravelRecentSearchTrackingRequestParam: DealsTravelRecentSearchTrackingRequest) {
        dealsTravelRecentSearchTrackingRequest = dealsTravelRecentSearchTrackingRequestParam
    }

    override suspend fun executeOnBackground(): Map<Type, RestResponse?> {
        val restRequest = RestRequest.Builder(url, DealsTrackingResponse::class.java)
            .setBody(dealsTravelRecentSearchTrackingRequest)
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

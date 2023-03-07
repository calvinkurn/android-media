package com.tokopedia.deals.pdp.domain

import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.common.network.coroutines.usecase.RestRequestUseCase
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.deals.pdp.data.DealsRatingUpdateRequest
import com.tokopedia.deals.pdp.data.DealsRatingUpdateResponse
import com.tokopedia.url.TokopediaUrl
import java.lang.reflect.Type
import javax.inject.Inject

class DealsPDPUpdateRatingUseCase @Inject constructor(private val repository: RestRepository) : RestRequestUseCase(repository) {

    private var url: String = TokopediaUrl.getInstance().BOOKING + DEALS_LIKES_UPDATE
    private var dealsRatingUpdateRequest: DealsRatingUpdateRequest = DealsRatingUpdateRequest()

    fun setParam(dealsRatingUpdateRequestParam: DealsRatingUpdateRequest) {
        dealsRatingUpdateRequest = dealsRatingUpdateRequestParam
    }

    override suspend fun executeOnBackground(): Map<Type, RestResponse?> {
        val restRequest = RestRequest.Builder(url, DealsRatingUpdateResponse::class.java)
            .setBody(dealsRatingUpdateRequest)
            .setRequestType(RequestType.POST)
            .build()
        restRequestList.clear()
        restRequestList.add(restRequest)
        return repository.getResponses(restRequestList)
    }

    companion object {
        var DEALS_LIKES_UPDATE = "v1/api/deal/rating"
    }
}

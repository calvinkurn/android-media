package com.tokopedia.deals.pdp.domain

import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.common.network.coroutines.usecase.RestRequestUseCase
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.deals.pdp.data.DealsRatingResponse
import com.tokopedia.url.TokopediaUrl
import java.lang.reflect.Type
import javax.inject.Inject

class DealsPDPGetRatingUseCase @Inject constructor(private val repository: RestRepository) : RestRequestUseCase(repository) {

    private var url: String = TokopediaUrl.getInstance().BOOKING + DEALS_LIKES

    fun setUrlId(id: String) {
        url = "$url/$id"
    }

    override suspend fun executeOnBackground(): Map<Type, RestResponse?> {
        val restRequest = RestRequest.Builder(url, DealsRatingResponse::class.java)
        .setRequestType(RequestType.GET)
            .build()
        restRequestList.clear()
        restRequestList.add(restRequest)
        return repository.getResponses(restRequestList)
    }

    companion object {
        var DEALS_LIKES = "v1/api/deal/rating/product"
    }
}

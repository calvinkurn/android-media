package com.tokopedia.entertainment.pdp.network_api

import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.common.network.coroutines.usecase.RestRequestUseCase
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.entertainment.pdp.data.redeem.redeemable.EventRedeemedData
import java.lang.reflect.Type
import javax.inject.Inject

class RedeemTicketEventUseCase @Inject
constructor(private val repository: RestRepository) : RestRequestUseCase(repository) {

    var url : String = ""

    fun setUrlRedeem(urlRedeem: String){
        url = urlRedeem
    }

    override suspend fun executeOnBackground(): Map<Type, RestResponse?> {
        val restRequest = RestRequest.Builder(url, EventRedeemedData::class.java)
                .setRequestType(RequestType.POST)
                .setBody("")
                .build()
        restRequestList.clear()
        restRequestList.add(restRequest)
        return repository.getResponses(restRequestList)

    }
}
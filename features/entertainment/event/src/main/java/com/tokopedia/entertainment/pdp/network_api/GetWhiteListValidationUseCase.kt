package com.tokopedia.entertainment.pdp.network_api

import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.common.network.coroutines.usecase.RestRequestUseCase
import com.tokopedia.entertainment.pdp.data.redeem.validate.EventValidateResponse
import com.tokopedia.entertainment.pdp.data.redeem.validate.EventValidateUser
import javax.inject.Inject
import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.common.network.data.model.RestResponse
import java.lang.reflect.Type

class GetWhiteListValidationUseCase @Inject
constructor(private val repository: RestRepository) : RestRequestUseCase(repository) {

    var eventValidateUser : EventValidateUser = EventValidateUser()

    fun setValidateUser(eventValidateUserParam: EventValidateUser){
        eventValidateUser = eventValidateUserParam
    }

    override suspend fun executeOnBackground(): Map<Type, RestResponse> {
        val url = EventCheckoutApi.BASE_URL + PATH_VALIDATE_REDEEM
        val restRequest = RestRequest.Builder(url, EventValidateResponse::class.java)
                .setBody(eventValidateUser)
                .setRequestType(RequestType.POST)
                .build()
        restRequestList.clear()
        restRequestList.add(restRequest)
        return repository.getResponses(restRequestList)
    }
}
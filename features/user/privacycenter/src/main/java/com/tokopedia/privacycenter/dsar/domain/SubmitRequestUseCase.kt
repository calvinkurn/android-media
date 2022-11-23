package com.tokopedia.privacycenter.dsar.domain

import com.tokopedia.privacycenter.dsar.model.CreateRequestBody
import com.tokopedia.privacycenter.dsar.model.CreateRequestResponse
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class SubmitRequestUseCase @Inject constructor(val oneTrustApi: OneTrustApi) :
    UseCase<CreateRequestResponse>() {

    override suspend fun executeOnBackground(): CreateRequestResponse {
        val token = oneTrustApi.getCredentials().body()!!
        val body = CreateRequestBody()
        return oneTrustApi.createRequest(body, "570ceffa-c436-4b8f-b5ac-3c2e5b010c31").body()!!
    }
}

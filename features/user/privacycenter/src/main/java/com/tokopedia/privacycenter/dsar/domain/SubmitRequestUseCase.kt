package com.tokopedia.privacycenter.dsar.domain

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.privacycenter.dsar.model.AdditionalData
import com.tokopedia.privacycenter.dsar.model.CreateRequestBody
import com.tokopedia.privacycenter.dsar.model.CreateRequestResponse
import com.tokopedia.privacycenter.dsar.model.UpdateRequestBody
import com.tokopedia.user.session.UserSessionInterface
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class SubmitRequestUseCase @Inject constructor(
    val userSession: UserSessionInterface,
    val oneTrustApi: OneTrustApi,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<CreateRequestBody, CreateRequestResponse>(dispatcher.io) {

    override fun graphqlQuery(): String = ""

    override suspend fun execute(params: CreateRequestBody): CreateRequestResponse {
        val token = oneTrustApi.getCredentials(
            "client_credentials".toRequestBody("text/plain".toMediaTypeOrNull()),
            "7505b9e763a44cd2b8a2c39072aba3a9".toRequestBody("text/plain".toMediaTypeOrNull()),
            "RKwtU9yHUEuQRiOhVp797cPZpy9zlKR9".toRequestBody("text/plain".toMediaTypeOrNull())
        ).body()?.accessToken ?: ""
        val request = oneTrustApi.createRequest(params, "570ceffa-c436-4b8f-b5ac-3c2e5b010c31", HeaderUtils.createHeader(
            token = token,
        )).body()
        oneTrustApi.updateRequest(
            UpdateRequestBody(), request?.requestQueueRefId ?: "", HeaderUtils.createHeader(
            token = token
        ))
        return request!!
    }

    fun constructParams(requestDetails: ArrayList<String>): CreateRequestBody {
        val additionalData = AdditionalData(
            userId = userSession.userId,
            requestDetails = requestDetails.joinToString(","),
            phoneNumber = "${userSession.phoneNumber}"
        )
        return CreateRequestBody(
            languange = "en-us",
            email = userSession.email,
            additionalData = additionalData,
            firstName = userSession.name,
            lastName = ""
        )
    }
}

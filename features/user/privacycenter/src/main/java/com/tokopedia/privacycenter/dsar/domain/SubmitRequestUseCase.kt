package com.tokopedia.privacycenter.dsar.domain

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.privacycenter.dsar.DsarConstants
import com.tokopedia.privacycenter.dsar.DsarHelper
import com.tokopedia.privacycenter.dsar.model.AdditionalData
import com.tokopedia.privacycenter.dsar.model.CreateRequestBody
import com.tokopedia.privacycenter.dsar.model.CreateRequestResponse
import com.tokopedia.privacycenter.dsar.model.UpdateRequestBody
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class SubmitRequestUseCase @Inject constructor(
    val userSession: UserSessionInterface,
    val oneTrustApi: OneTrustApi,
    val getCredentialsApi: GetCredentialsApi,
    val dsarHelper: DsarHelper,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<CreateRequestBody, CreateRequestResponse>(dispatcher.io) {

    override fun graphqlQuery(): String = ""

    override suspend fun execute(params: CreateRequestBody): CreateRequestResponse {
        val credentials = getCredentialsApi.fetchCredential()
        credentials?.let {
            val request = oneTrustApi.createRequest(params, dsarHelper.getTemplateId(), HeaderUtils.createHeader(
                token = it.accessToken,
            )).body()
            oneTrustApi.updateRequest(
                UpdateRequestBody(), request?.requestQueueRefId ?: "", HeaderUtils.createHeader(
                    token = it.accessToken
                ))
            return request!!
        }
        return CreateRequestResponse()
    }

    fun constructParams(requestDetails: ArrayList<String>): CreateRequestBody {
        val additionalData = AdditionalData(
            userId = userSession.userId,
            requestDetails = requestDetails.joinToString(","),
            phoneNumber = userSession.phoneNumber
        )
        return CreateRequestBody(
            languange = DsarConstants.LANG_US,
            email = userSession.email,
            additionalData = additionalData,
            firstName = userSession.name,
            lastName = "",
            templateId = dsarHelper.getTemplateId()
        )
    }
}

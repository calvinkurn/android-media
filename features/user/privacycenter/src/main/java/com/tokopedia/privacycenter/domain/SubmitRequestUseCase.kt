package com.tokopedia.privacycenter.domain

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.privacycenter.data.AdditionalData
import com.tokopedia.privacycenter.data.CreateRequestBody
import com.tokopedia.privacycenter.data.CreateRequestResponse
import com.tokopedia.privacycenter.data.UpdateRequestBody
import com.tokopedia.privacycenter.remote.GetCredentialsApi
import com.tokopedia.privacycenter.remote.HeaderUtils
import com.tokopedia.privacycenter.remote.OneTrustApi
import com.tokopedia.privacycenter.ui.dsar.DsarConstants
import com.tokopedia.privacycenter.ui.dsar.DsarHelper
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
            if (it.accessToken.isNotEmpty()) {
                val request = oneTrustApi.createRequest(
                    params,
                    dsarHelper.getTemplateId(),
                    HeaderUtils.createHeader(
                        token = it.accessToken
                    )
                ).body()
                oneTrustApi.updateRequest(
                    UpdateRequestBody(),
                    request?.requestQueueRefId ?: "",
                    HeaderUtils.createHeader(
                        token = it.accessToken
                    )
                )
                if (request != null) {
                    return request
                }
            }
        }
        throw MessageErrorException(DsarConstants.LABEL_ERROR_REQUEST)
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

package com.tokopedia.updateinactivephone.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.updateinactivephone.domain.data.InactivePhoneUserDataModel
import com.tokopedia.updateinactivephone.domain.data.SubmitExpeditedDataModel
import javax.inject.Inject

open class SubmitExpeditedInactivePhoneUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<InactivePhoneUserDataModel, SubmitExpeditedDataModel>(dispatcher.io) {

    override suspend fun execute(params: InactivePhoneUserDataModel): SubmitExpeditedDataModel {
        return repository.request(graphqlQuery(), createParams(params))
    }

    override fun graphqlQuery(): String {
        return """
            query SubmitExpeditedInactivePhone(${'$'}userIDEnc: String!, ${'$'}validateToken: String!, ${'$'}msisdn: String!) {
              SubmitExpeditedInactivePhone(userIDEnc: ${'$'}userIDEnc, validateToken: ${'$'}validateToken, msisdn: ${'$'}msisdn) {
                message_error
                is_success
              }
            }
        """.trimIndent()
    }

    private fun createParams(params: InactivePhoneUserDataModel): Map<String, Any> = mapOf(
        PARAM_MSISDN to params.newPhoneNumber,
        PARAM_VALIDATE_TOKEN to params.validateToken,
        PARAM_USER_ID_ENC to params.userIdEnc
    )

    companion object {
        private const val PARAM_MSISDN = "msisdn"
        private const val PARAM_VALIDATE_TOKEN = "validateToken"
        private const val PARAM_USER_ID_ENC = "userIDEnc"
    }
}
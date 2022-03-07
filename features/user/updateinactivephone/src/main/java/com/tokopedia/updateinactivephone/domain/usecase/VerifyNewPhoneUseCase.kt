package com.tokopedia.updateinactivephone.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.updateinactivephone.domain.data.InactivePhoneUserDataModel
import com.tokopedia.updateinactivephone.domain.data.VerifyNewPhoneDataModel
import javax.inject.Inject

open class VerifyNewPhoneUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
): CoroutineUseCase<InactivePhoneUserDataModel, VerifyNewPhoneDataModel>(dispatcher.io) {

    override suspend fun execute(params: InactivePhoneUserDataModel): VerifyNewPhoneDataModel {
        return repository.request(graphqlQuery(), createParams(params))
    }

    override fun graphqlQuery(): String {
        return """
            mutation verifyNewPhoneInactivePhoneUser(${'$'}msisdn : String!, ${'$'}userIDEnc: String!, ${'$'}validateToken: String!) {
              verifyNewPhoneInactivePhoneUser(msisdn: ${'$'}msisdn, userIDEnc: ${'$'}userIDEnc, validateToken: ${'$'}validateToken) {
                isSuccess
            	errorMessage
              }
            }
        """.trimIndent()
    }

    private fun createParams(params: InactivePhoneUserDataModel): Map<String, Any> = mapOf(
        PARAM_MSISDN to params.newPhoneNumber,
        PARAM_USER_ID_ENC to params.userIdEnc,
        PARAM_VALIDATE_TOKEN to params.validateToken
    )

    companion object {
        private const val PARAM_USER_ID_ENC = "userIDEnc"
        private const val PARAM_MSISDN = "msisdn"
        private const val PARAM_VALIDATE_TOKEN = "validateToken"
    }
}
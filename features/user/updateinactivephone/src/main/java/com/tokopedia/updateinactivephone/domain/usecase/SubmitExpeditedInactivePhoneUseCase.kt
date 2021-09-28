package com.tokopedia.updateinactivephone.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.updateinactivephone.domain.data.SubmitExpeditedInactivePhoneDataModel
import javax.inject.Inject

open class SubmitExpeditedInactivePhoneUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<Map<String, Any>, SubmitExpeditedInactivePhoneDataModel>(dispatcher.io) {

    override fun graphqlQuery(): String {
        return query
    }

    override suspend fun execute(params: Map<String, Any>): SubmitExpeditedInactivePhoneDataModel {
        return request(repository, params)
    }

    companion object {
        const val PARAM_MSISDN = "msisdn"
        const val PARAM_VALIDATE_TOKEN = "validateToken"
        const val PARAM_USER_ID_ENC = "userIDEnc"

        private val query = """
            query SubmitExpeditedInactivePhone(${'$'}userIDEnc: String, ${'$'}validateToken: String, ${'$'}msisdn: String) {
              SubmitExpeditedInactivePhone(userIDEnc: ${'$'}userIDEnc, validateToken: ${'$'}validateToken, msisdn: ${'$'}msisdn) {
                message_error
                is_success
              }
            }
        """.trimIndent()
    }
}
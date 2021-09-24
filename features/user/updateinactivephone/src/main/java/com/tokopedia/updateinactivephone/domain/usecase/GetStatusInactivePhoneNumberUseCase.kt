package com.tokopedia.updateinactivephone.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.updateinactivephone.domain.data.StatusInactivePhoneNumberDataModel

open class GetStatusInactivePhoneNumberUseCase constructor(
    private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<Map<String, Any>, StatusInactivePhoneNumberDataModel>(dispatcher.io){

    override fun graphqlQuery(): String {
        return query
    }

    override suspend fun execute(params: Map<String, Any>): StatusInactivePhoneNumberDataModel {
        return request(repository, params)
    }

    companion object {
        const val PARAM_EMAIL = "email"
        const val PARAM_PHONE = "msisdn"
        const val PARAM_USER_INDEX = "index"

        private val query = """
            query GetStatusInactivePhoneNumber(${'$'}email: String, ${'$'}msisdn: String, ${'$'}index: Int) {
              GetStatusInactivePhoneNumber(email: ${'$'}email, msisdn: ${'$'}msisdn, index: ${'$'}index) {
                error_message
                is_success
                is_allowed
                userid_enc
              }
            }
        """.trimIndent()
    }
}
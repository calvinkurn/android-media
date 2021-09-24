package com.tokopedia.updateinactivephone.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.updateinactivephone.domain.data.SubmitExpeditedInactivePhoneDataModel

open class SubmitExpeditedInactivePhoneUseCase constructor(
    private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<Map<String, Any>, SubmitExpeditedInactivePhoneDataModel>(dispatcher.io) {

    override fun graphqlQuery(): String {
        return query
    }

    override suspend fun execute(params: Map<String, Any>): SubmitExpeditedInactivePhoneDataModel {
        return request(repository, params)
    }

    companion object {
        const val PARAM_EMAIL = "email"
        const val PARAM_MSISDN = "msisdn"
        const val PARAM_USER_INDEX = "index"

        private val query = """
            query SubmitExpeditedInactivePhone(${'$'}msisdn: String, ${'$'}email: String, ${'$'}index: String) {
              SubmitExpeditedInactivePhone(msisdn: ${'$'}msisdn, email: ${'$'}email, index: ${'$'}index) {
                message_error
                is_success
              }
            }
        """.trimIndent()
    }
}
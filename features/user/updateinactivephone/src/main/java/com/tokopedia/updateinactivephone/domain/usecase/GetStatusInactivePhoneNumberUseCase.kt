package com.tokopedia.updateinactivephone.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.updateinactivephone.domain.data.StatusInactivePhoneNumberDataModel

class GetStatusInactivePhoneNumberUseCase constructor(
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

        private val query = """
            query GetStatusInactivePhoneNumber(${'$'}email: String!) {
              GetStatusInactivePhoneNumber(email: ${'$'}email){
                error_message
                is_success
                is_allowed
                userid_enc
              }
            }
        """.trimIndent()
    }
}
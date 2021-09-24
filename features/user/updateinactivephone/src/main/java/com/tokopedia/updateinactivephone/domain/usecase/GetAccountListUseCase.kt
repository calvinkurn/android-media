package com.tokopedia.updateinactivephone.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.updateinactivephone.domain.data.AccountListDataModel

open class GetAccountListUseCase constructor(
    private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<Map<String, Any>, AccountListDataModel>(dispatcher.io) {

    override suspend fun execute(params: Map<String, Any>): AccountListDataModel {
        return request(repository, params)
    }

    override fun graphqlQuery(): String {
        return query
    }

    companion object {
        const val PARAM_PHONE_NUMBER = "phone"
        const val PARAM_VALIDATE_TOKEN = "validate_token"
        const val PARAM_IS_INACTIVE_PHONE = "is_inactive_phone"

        private val query = """
            query accountsGetAccountsList(${'$'}validate_token : String!, ${'$'}phone : String, ${'$'}is_inactive_phone : Boolean) {
              accountsGetAccountsList(validate_token: ${'$'}validate_token, phone : ${'$'}phone, is_inactive_phone : ${'$'}is_inactive_phone) {
                key
                msisdn_view
                msisdn
                users_details {
                  fullname
                  email
                  image
                  user_id_index
                }
                errors {
                  name
                  message
                }
              }
            }
        """.trimIndent()
    }
}
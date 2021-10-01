package com.tokopedia.updateinactivephone.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.updateinactivephone.domain.data.AccountListDataModel
import javax.inject.Inject

open class GetAccountListUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<String, AccountListDataModel>(dispatcher.io) {

    override suspend fun execute(params: String): AccountListDataModel {
        return repository.request(graphqlQuery(), createParams(params))
    }

    override fun graphqlQuery(): String {
        return query
    }

    private fun createParams(phoneNumber: String): Map<String, Any> = mapOf(
        PARAM_PHONE_NUMBER to phoneNumber,
        PARAM_VALIDATE_TOKEN to "",
        PARAM_IS_INACTIVE_PHONE to true
    )

    companion object {
        private const val PARAM_PHONE_NUMBER = "phone"
        private const val PARAM_VALIDATE_TOKEN = "validate_token"
        private const val PARAM_IS_INACTIVE_PHONE = "is_inactive_phone"

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
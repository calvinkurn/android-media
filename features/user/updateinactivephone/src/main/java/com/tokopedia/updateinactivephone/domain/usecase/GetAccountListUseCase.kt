package com.tokopedia.updateinactivephone.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.updateinactivephone.domain.data.AccountListDataModel
import javax.inject.Inject

class GetAccountListUseCase @Inject constructor(
        private val graphqlUseCase: GraphqlUseCase<AccountListDataModel>
) {

    lateinit var params: Map<String, Any>

    fun execute(onSuccess: (AccountListDataModel) -> Unit, onError: (Throwable) -> Unit) {
        graphqlUseCase.apply {
            setTypeClass(AccountListDataModel::class.java)
            setGraphqlQuery(query)
            setRequestParams(params)
            execute(onSuccess = {
                onSuccess(it)
            }, onError = {
                onError(it)
            })
        }
    }

    fun generateParam(phoneNumber: String) {
        params = mapOf(
                PARAM_PHONE_NUMBER to phoneNumber,
                PARAM_IS_INACTIVE_PHONE to true,
                PARAM_VALIDATE_TOKEN to ""
        )
    }

    fun cancelJobs() {
        graphqlUseCase.cancelJobs()
        graphqlUseCase.clearCache()
    }

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
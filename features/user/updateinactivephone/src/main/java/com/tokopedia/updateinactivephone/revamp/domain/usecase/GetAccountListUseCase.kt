package com.tokopedia.updateinactivephone.revamp.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.updateinactivephone.revamp.domain.data.AccountListDataModel
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

    fun cancelJobs() {
        graphqlUseCase.cancelJobs()
    }

    companion object {
        private val query = """
        query get_accounts_list(${'$'}validate_token : String!, ${'$'}phone : String!) {
          accountsGetAccountsList(validate_token: ${'$'}validate_token, phone : ${'$'}phone) {
            key
            msisdn_view
            msisdn
            users_details {
              user_id
              fullname
              email
              msisdn_verified
              image
              challenge_2fa
              user_id_enc
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
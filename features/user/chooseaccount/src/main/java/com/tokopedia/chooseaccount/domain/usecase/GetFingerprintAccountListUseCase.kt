package com.tokopedia.chooseaccount.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.chooseaccount.data.AccountsDataModel
import com.tokopedia.chooseaccount.di.ChooseAccountQueryConstant
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

/**
 * Created by Yoris on 07/09/21.
 */

class GetFingerprintAccountListUseCase(
    val graphqlUseCase: GraphqlUseCase<AccountsDataModel>,
    var dispatchers: CoroutineDispatchers
): CoroutineScope {
    override val coroutineContext: CoroutineContext get() = dispatchers.main + SupervisorJob()

    fun getAccounts(validateToken: String, loginType: String, deviceBiometrics: String, onSuccess: (AccountsDataModel) -> Unit, onError: (Throwable) -> Unit) {
        launchCatchError(dispatchers.io, {
            val data =
                graphqlUseCase.apply {
                    setTypeClass(AccountsDataModel::class.java)
                    setGraphqlQuery(query)
                    setRequestParams(createRequestParams(validateToken, loginType, deviceBiometrics))
                }.executeOnBackground()
            withContext(dispatchers.main) {
                onSuccess(data)
            }
        }, {
            withContext(dispatchers.main) {
                onError(it)
            }
        })
    }

    private fun createRequestParams(validateToken: String, loginType: String, deviceBiometrics: String): Map<String, Any>{
        return mapOf(
            ChooseAccountQueryConstant.PARAM_VALIDATE_TOKEN to validateToken,
            ChooseAccountQueryConstant.PARAM_PHONE to "",
            ChooseAccountQueryConstant.PARAM_LOGIN_TYPE to loginType,
            ChooseAccountQueryConstant.PARAM_DEVICE_BIOMETRIC to deviceBiometrics
        )
    }

    companion object {
        val query: String = """
            query get_accounts_list(${'$'}validate_token: String!, ${'$'}phone: String!, ${'$'}login_type: String!, ${'$'}device_biometrics: String!) {
              accountsGetAccountsList(validate_token: ${'$'}validate_token, phone: ${'$'}phone, login_type: ${'$'}login_type, device_biometrics: ${'$'}device_biometrics) {
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
                  shop_detail {
                    id
                    name
                    domain
                  }
                }
                users_count
                errors {
                  name
                  message
                }
              }
            }
            """.trimIndent()
    }
}
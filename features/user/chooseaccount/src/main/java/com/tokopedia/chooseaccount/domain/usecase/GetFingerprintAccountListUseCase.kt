package com.tokopedia.chooseaccount.domain.usecase

import com.tokopedia.chooseaccount.data.AccountsDataModel
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

/**
 * Created by Yoris on 07/10/21.
 */

class GetFingerprintAccountListUseCase @Inject constructor(
    private val repository: GraphqlRepository
): CoroutineUseCase<Map<String, String>, AccountsDataModel>(Dispatchers.IO){

    override suspend fun execute(params: Map<String, String>): AccountsDataModel {
        return repository.request(graphqlQuery(), params)
    }

    override fun graphqlQuery(): String = """
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
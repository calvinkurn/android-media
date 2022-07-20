package com.tokopedia.updateinactivephone.domain.usecase

import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.GqlParam
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.updateinactivephone.domain.data.AccountListDataModel
import javax.inject.Inject

open class GetAccountListUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<GetAccountListParam, AccountListDataModel>(dispatcher.io) {

    override suspend fun execute(params: GetAccountListParam): AccountListDataModel {
        return repository.request(graphqlQuery(), params.toMapParam())
    }

    override fun graphqlQuery(): String {
        return """
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

data class GetAccountListParam(
    @SerializedName("phone")
    var phone: String = "",
    @SerializedName("validate_token")
    var validateToken: String = "",
    @SerializedName("is_inactive_phone")
    var isInactivePhone: Boolean = false,
): GqlParam
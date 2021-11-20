package com.tokopedia.managepassword.addpassword.domain.usecase

import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.GqlParam
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.managepassword.addpassword.domain.data.AddPasswordV2Response
import javax.inject.Inject

class AddPasswordV2UseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<AddPasswordV2Params, AddPasswordV2Response>(dispatchers.io) {

    override suspend fun execute(params: AddPasswordV2Params): AddPasswordV2Response {
        return repository.request(graphqlQuery(), params)
    }

    override fun graphqlQuery(): String {
        return """
        mutation addPassword(${'$'}password: String!, ${'$'}password_confirm: String!, ${'$'}h: String!) {
          addPasswordV2(password: ${'$'}password, password_confirm: ${'$'}password_confirm, h: ${'$'}h) {
            is_success
            error_code
            error_message
          }
        }    
        """.trimIndent()
    }
}

data class AddPasswordV2Params(
    @SerializedName("password")
    var password: String? = "",
    @SerializedName("password_confirm")
    var passwordConfirmation: String? = "",
    @SerializedName("h")
    var hash: String? = ""
) : GqlParam
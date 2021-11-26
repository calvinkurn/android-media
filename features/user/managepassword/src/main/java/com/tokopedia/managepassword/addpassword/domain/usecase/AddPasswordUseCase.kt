package com.tokopedia.managepassword.addpassword.domain.usecase

import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.GqlParam
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.managepassword.addpassword.domain.data.AddPasswordResponseModel
import javax.inject.Inject

class AddPasswordUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<AddPasswordParam, AddPasswordResponseModel>(dispatchers.io) {

    override suspend fun execute(params: AddPasswordParam): AddPasswordResponseModel {
        return repository.request(graphqlQuery(), params)
    }

    override fun graphqlQuery(): String {
        return """
            mutation addPassword(${'$'}password: String!, ${'$'}password_confirm: String!) {
              addPassword(password: ${'$'}password, password_confirm: ${'$'}password_confirm) {
                is_success
                error_code
                error_message
              }
            }
        """.trimIndent()
    }
}

data class AddPasswordParam(
    @SerializedName("password")
    var password: String? = "",
    @SerializedName("password_confirm")
    var passwordConfirmation: String? = ""
) : GqlParam
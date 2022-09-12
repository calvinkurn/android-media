package com.tokopedia.loginregister.redefine_register_email.view.input_phone.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.loginregister.redefine_register_email.view.input_phone.domain.data.UserProfileUpdateModel
import com.tokopedia.loginregister.redefine_register_email.view.input_phone.domain.data.UserProfileUpdateParam
import javax.inject.Inject

class UserProfileUpdateUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<UserProfileUpdateParam, UserProfileUpdateModel>(dispatchers.io) {
    override fun graphqlQuery(): String =
        """
          mutation userProfileUpdate(${'$'}phone: String!, ${'$'}currValidateToken: String!) {
            userProfileUpdate(phone: ${'$'}phone, currValidateToken: ${'$'}currValidateToken) {
              isSuccess
              errors
            }
          }
        """.trimIndent()

    override suspend fun execute(params: UserProfileUpdateParam): UserProfileUpdateModel {
        return repository.request(graphqlQuery(), params)
    }
}
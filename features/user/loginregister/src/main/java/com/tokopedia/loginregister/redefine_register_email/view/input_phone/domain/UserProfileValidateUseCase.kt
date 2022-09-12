package com.tokopedia.loginregister.redefine_register_email.view.input_phone.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.loginregister.redefine_register_email.view.input_phone.domain.data.UserProfileValidateModel
import com.tokopedia.loginregister.redefine_register_email.view.input_phone.domain.data.UserProfileValidateParam
import javax.inject.Inject

class UserProfileValidateUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<UserProfileValidateParam, UserProfileValidateModel>(dispatchers.io) {

    override fun graphqlQuery(): String =
        """
            mutation userProfileValidate(${'$'}phone: String, ${'$'}email: String) {
                userProfileValidate(phone: ${'$'}phone, email: ${'$'}email) {
                    isValid,
                    message
                }
            }
        """.trimIndent()

    override suspend fun execute(params: UserProfileValidateParam): UserProfileValidateModel {
        return repository.request(graphqlQuery(), params)
    }

}
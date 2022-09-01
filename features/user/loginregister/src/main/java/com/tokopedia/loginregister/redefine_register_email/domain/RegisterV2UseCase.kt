package com.tokopedia.loginregister.redefine_register_email.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.loginregister.redefine_register_email.domain.data.RegisterV2Model
import com.tokopedia.loginregister.redefine_register_email.domain.data.RegisterV2Param
import javax.inject.Inject

class RegisterV2UseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<RegisterV2Param, RegisterV2Model>(dispatchers.io) {
    override fun graphqlQuery(): String =
        """
            mutation register(${'$'}reg_type: String!, ${'$'}fullname: String!, ${'$'}email: String!, ${'$'}phone: String!, ${'$'}password: String!, ${'$'}validate_token: String!, ${'$'}captcha_token: String!, ${'$'}h: String!) {
                register_v2(input: {
                    reg_type        : ${'$'}reg_type
                    fullname        : ${'$'}fullname
                    email           : ${'$'}email
                    phone           : ${'$'}phone
                    password        : ${'$'}password
                    validate_token  : ${'$'}validate_token
                    captcha_token   : ${'$'}captcha_token
                    h               : ${'$'}h
                }) {
                    user_id
                    is_active
                    access_token
                    refresh_token
                    sid
                    enable_2fa
                    enable_skip_2fa
                    errors {
                      name
                      message
                    }
                    popup_error {
                      header
                      body
                      action
                    }
                }
            }
        """.trimIndent()

    override suspend fun execute(params: RegisterV2Param): RegisterV2Model {
        return repository.request(graphqlQuery(), params)
    }
}
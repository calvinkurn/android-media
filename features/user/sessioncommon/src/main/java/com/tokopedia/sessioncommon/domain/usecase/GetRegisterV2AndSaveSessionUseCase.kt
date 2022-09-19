package com.tokopedia.sessioncommon.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.sessioncommon.data.register.Register
import com.tokopedia.sessioncommon.data.register.RegisterV2Model
import com.tokopedia.sessioncommon.data.register.RegisterV2Param
import com.tokopedia.sessioncommon.domain.commonaction.RegisterV2SaveSession
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class GetRegisterV2AndSaveSessionUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    private val userSession: UserSessionInterface,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<RegisterV2Param, Result<Register>>(dispatchers.io) {
    override fun graphqlQuery(): String =
        """
            mutation register(${'$'}reg_type: String!, ${'$'}fullname: String!, ${'$'}email: String!, ${'$'}phone: String!, ${'$'}password: String!, ${'$'}validate_token: String!, ${'$'}h: String!) {
                register_v2(input: {
                    reg_type        : ${'$'}reg_type
                    fullname        : ${'$'}fullname
                    email           : ${'$'}email
                    phone           : ${'$'}phone
                    password        : ${'$'}password
                    validate_token  : ${'$'}validate_token
                    h               : ${'$'}h
                }) {
                    user_id
                    is_active
                    access_token
                    refresh_token
                    token_type
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

    override suspend fun execute(params: RegisterV2Param): Result<Register> {
        val response: RegisterV2Model = repository.request(graphqlQuery(), params)

        return object : RegisterV2SaveSession(response, userSession) {}.data()
    }
}
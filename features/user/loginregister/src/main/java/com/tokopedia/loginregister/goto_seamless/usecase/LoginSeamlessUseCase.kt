package com.tokopedia.loginregister.goto_seamless.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.loginregister.goto_seamless.model.LoginSeamlessParams
import com.tokopedia.sessioncommon.data.LoginTokenPojoV2
import javax.inject.Inject

class LoginSeamlessUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
): CoroutineUseCase<LoginSeamlessParams, LoginTokenPojoV2>(dispatchers.io) {

    override suspend fun execute(params: LoginSeamlessParams): LoginTokenPojoV2 {
        return repository.request(graphqlQuery(), params)
    }

    override fun graphqlQuery(): String = """
        mutation login_seamless(${'$'}grant_type: String!, ${'$'}code: String!){
            login_token_v2(
                input: {
                    grant_type: ${'$'}grant_type
                    code: ${'$'}code
                }
            ) {
                acc_sid
                access_token
                expires_in
                refresh_token
                sid
                token_type
                sq_check
                action
                errors {
                    name
                    message
                }
                event_code
            }
        }
    """.trimIndent()

    companion object {
        const val GRANT_TYPE_AUTH_CODE = "authorization_code"
    }
}
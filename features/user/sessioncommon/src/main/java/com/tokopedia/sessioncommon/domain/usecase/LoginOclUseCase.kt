package com.tokopedia.sessioncommon.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.refreshtoken.EncoderDecoder
import com.tokopedia.sessioncommon.data.LoginToken
import com.tokopedia.sessioncommon.data.LoginTokenPojoV2
import com.tokopedia.sessioncommon.data.ocl.LoginOclParam
import com.tokopedia.sessioncommon.util.TokenGenerator
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class LoginOclUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    val userSessionInterface: UserSessionInterface,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<LoginOclParam, LoginToken>(dispatcher.io) {

    override fun graphqlQuery(): String = """
        mutation login_email_v2(${'$'}grant_type: String!, ${'$'}ocl_jwt_token: String!, ${'$'}access_token: String!, ${'$'}social_type: String!){
            login_token_v2(
                input: {
                    grant_type: ${'$'}grant_type
                    ocl_jwt_token: ${'$'}ocl_jwt_token
                    access_token: ${'$'}access_token
                    social_type: ${'$'}social_type
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
                popup_error {
                    header
                    body
                    action
                }
            }
        }
    """.trimIndent()

    override suspend fun execute(params: LoginOclParam): LoginToken {
        userSessionInterface.setToken(TokenGenerator().createBasicTokenGQL(), "")
        val result: LoginTokenPojoV2 = repository.request(graphqlQuery(), params)
        if (result.loginToken.errors.isEmpty()) {
            saveAccessToken(result.loginToken)
            return result.loginToken
        } else {
            throw MessageErrorException(result.loginToken.errors.first().message)
        }
    }

    private fun saveAccessToken(loginToken: LoginToken?) {
        loginToken?.run {
            userSessionInterface.setToken(
                accessToken,
                tokenType,
                EncoderDecoder.Encrypt(refreshToken, userSessionInterface.refreshTokenIV)
            )
        }
    }
}

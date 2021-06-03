package com.tokopedia.loginfingerprint.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sessioncommon.data.LoginToken
import com.tokopedia.sessioncommon.data.LoginTokenPojo
import com.tokopedia.sessioncommon.di.SessionModule
import com.tokopedia.sessioncommon.domain.mapper.LoginV2Mapper
import com.tokopedia.sessioncommon.util.TokenGenerator
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named
import kotlin.coroutines.CoroutineContext

class LoginFingerprintUseCase @Inject constructor(
    private val graphqlUseCase: GraphqlUseCase<LoginTokenPojo>,
    private var dispatchers: CoroutineDispatchers,
    @Named(SessionModule.SESSION_MODULE)
    private val userSession: UserSessionInterface
): CoroutineScope {

    override val coroutineContext: CoroutineContext get() = dispatchers.main + SupervisorJob()

    fun loginBiometric(email: String,
                       validateToken: String,
                       onSuccessLoginToken: (loginTokenPojo: LoginToken) -> Unit,
                       onErrorLoginToken: (e: Throwable) -> Unit,
                       onShowPopupError: (loginTokenPojo: LoginToken)  -> Unit,
                       onGoToActivationPage: (errorMessage: MessageErrorException) -> Unit,
                       onGoToSecurityQuestion: () -> Unit) {
            launchCatchError(dispatchers.io, {
                userSession.setToken(TokenGenerator().createBasicTokenGQL(), "")
                val data =
                    graphqlUseCase.apply {
                        setTypeClass(LoginTokenPojo::class.java)
                        setGraphqlQuery(VerifyFingerprintUseCase.query)
                        setRequestParams(createRequestParams(email, validateToken))
                    }.executeOnBackground()
                withContext(dispatchers.main) {
                    LoginV2Mapper(userSession).map(data.loginToken, onSuccessLoginToken, onErrorLoginToken, onShowPopupError, onGoToActivationPage, onGoToSecurityQuestion)
                }
            }, {
                withContext(dispatchers.main) {
                    onErrorLoginToken(it)
                }
            })
    }

    private fun createRequestParams(email: String, validateToken: String): Map<String, Any> {
        return mapOf(
            PARAM_GRANT_TYPE to TokenGenerator().encode(TYPE_EXTENSION),
            PARAM_SOCIAL_TYPE to SOCIAL_TYPE_BIOMETRIC,
            PARAM_USERNAME to TokenGenerator().encode(email),
            PARAM_VALIDATE_TOKEN to validateToken
        )
    }

    companion object {
        const val PARAM_GRANT_TYPE = "grant_type"
        const val PARAM_SOCIAL_TYPE = "social_type"
        const val PARAM_USERNAME = "username"
        const val PARAM_VALIDATE_TOKEN = "validate_token"

        const val SOCIAL_TYPE_BIOMETRIC = "14"
        const val TYPE_EXTENSION = "extension"

        val query: String = """
            mutation login_after_sq(${'$'}grant_type: String!, ${'$'}social_type: String!, ${'$'}username: String!, ${'$'}validate_token: String!) {
              login_token(input: {grant_type: ${'$'}grant_type, social_type: ${'$'}social_type, username: ${'$'}username, validate_token: ${'$'}validate_token)
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
    }
}
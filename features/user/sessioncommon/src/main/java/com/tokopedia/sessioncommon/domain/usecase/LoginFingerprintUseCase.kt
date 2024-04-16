package com.tokopedia.sessioncommon.domain.usecase

import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.GqlParam
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.sessioncommon.data.LoginTokenPojo
import com.tokopedia.sessioncommon.util.TokenGenerator
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class LoginFingerprintUseCase @Inject constructor(
    dispatchers: CoroutineDispatchers,
    @ApplicationContext private val repository: GraphqlRepository,
    private val userSession: UserSessionInterface
) : CoroutineUseCase<FingerPrintGqlParam, LoginTokenPojo>(dispatchers.io) {

    override fun graphqlQuery(): String {
        return """
            mutation login_biometric(${'$'}grant_type: String!, ${'$'}social_type: String!, ${'$'}username: String!, ${'$'}validate_token: String!, ${'$'}device_biometrics: String!) {
              login_token(
                input: {
                    grant_type: ${'$'}grant_type, 
                    social_type: ${'$'}social_type, 
                    username: ${'$'}username, 
                    validate_token: ${'$'}validate_token,
                    device_biometrics: ${'$'}device_biometrics
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
    }

    override suspend fun execute(params: FingerPrintGqlParam): LoginTokenPojo {
        userSession.setToken(TokenGenerator().createBasicTokenGQL(), "")
        return repository.request(graphqlQuery(), params)
    }

    companion object {
        const val PARAM_VALIDATE_TOKEN = "validate_token"
        const val SOCIAL_TYPE_BIOMETRIC = "14"
        const val TYPE_EXTENSION = "extension"
    }
}

data class FingerPrintGqlParam(
    @SerializedName("grant_type")
    val grantType: String,

    @SerializedName("social_type")
    val socialType: String,

    @SerializedName("username")
    val username: String,

    @SerializedName("validate_token")
    val validateToken: String,

    @SerializedName("device_biometrics")
    val deviceBiometrics: String
) : GqlParam

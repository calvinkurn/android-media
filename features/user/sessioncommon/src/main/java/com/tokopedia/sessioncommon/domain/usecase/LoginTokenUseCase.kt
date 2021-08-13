package com.tokopedia.sessioncommon.domain.usecase

import android.content.res.Resources
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.sessioncommon.R
import com.tokopedia.sessioncommon.data.LoginTokenPojo
import com.tokopedia.sessioncommon.di.SessionModule
import com.tokopedia.sessioncommon.util.TokenGenerator
import com.tokopedia.user.session.UserSessionInterface
import rx.Subscriber
import javax.inject.Inject
import javax.inject.Named

/**
 * @author by nisie on 28/05/19.
 */

open class LoginTokenUseCase @Inject constructor(val resources: Resources,
                                            private val graphqlUseCase: GraphqlUseCase,
                                            @Named(SessionModule.SESSION_MODULE)
                                            private val userSession: UserSessionInterface
) {

    fun executeLoginEmailWithPassword(requestParams: Map<String, Any>, subscriber:
    Subscriber<GraphqlResponse>) {
        userSession.setToken(TokenGenerator().createBasicTokenGQL(), "")
        execute(requestParams, subscriber, R.raw.mutation_login_email)
    }

    fun executeLoginAfterSQ(requestParams: Map<String, Any>, subscriber:
    Subscriber<GraphqlResponse>) {
        userSession.setToken(TokenGenerator().createBasicTokenGQL(), "")
        execute(requestParams, subscriber, R.raw.mutation_login_after_sq)
    }

    fun executeLoginFingerprint(requestParams: Map<String, Any>, subscriber:
    Subscriber<GraphqlResponse>) {
        userSession.setToken(TokenGenerator().createBasicTokenGQL(), "")
        execute(requestParams, subscriber, R.raw.mutation_login_fingerprint)
    }

    fun executeLoginSocialMedia(requestParams: Map<String, Any>, subscriber:
    Subscriber<GraphqlResponse>) {
        userSession.setToken(TokenGenerator().createBasicTokenGQL(), "")
        execute(requestParams, subscriber, R.raw.mutation_login_social_media)
    }

    fun executeLoginSocialMediaPhone(requestParams: Map<String, Any>, subscriber:
    Subscriber<GraphqlResponse>) {
        userSession.setToken(TokenGenerator().createBasicTokenGQL(), "")
        execute(requestParams, subscriber, R.raw.mutation_login_social_media_phone)
    }

    fun executeLoginPhoneNumber(requestParams: Map<String, Any>, subscriber:
    Subscriber<GraphqlResponse>) {
        userSession.setToken(TokenGenerator().createBasicTokenGQL(), "")
        execute(requestParams, subscriber, R.raw.mutation_login_phone)
    }

    fun executeLoginTokenSeamless(requestParams: Map<String, Any>, subscriber:
    Subscriber<GraphqlResponse>) {
        userSession.setToken(TokenGenerator().createBasicTokenGQL(), "")
        execute(requestParams, subscriber, R.raw.mutation_login_token_seamless)
    }

    open fun execute(requestParams: Map<String, Any>, subscriber:
    Subscriber<GraphqlResponse>, resId : Int) {
        val query = GraphqlHelper.loadRawString(resources, resId)
        val graphqlRequest = GraphqlRequest(query,
                LoginTokenPojo::class.java, requestParams)

        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        graphqlUseCase.execute(subscriber)
    }

    companion object {

        private val PARAM_INPUT: String = "input"
        private val PARAM_GRANT_TYPE: String = "grant_type"
        private val PARAM_PASSWORD_TYPE: String = "password_type"
        private val PARAM_SUPPORTED: String = "supported"
        private val PARAM_SOCIAL_TYPE: String = "social_type"
        private val PARAM_USERNAME: String = "username"
        private val PARAM_PASSWORD: String = "password"
        private val PARAM_ACCESS_TOKEN: String = "access_token"
        private val PARAM_REFRESH_TOKEN: String = "refresh_token"
        private val PARAM_VALIDATE_TOKEN: String = "validate_token"
        private val PARAM_CODE: String = "code"


        private val TYPE_PASSWORD:String = "password"
        private val TYPE_FINGERPRINT:String = "fingerprint"
        private val TYPE_EXTENSION:String = "extension"
        private val TYPE_OTP:String = "otp"
        private val TYPE_LPN:String = "lpn"

        val SOCIAL_TYPE_FACEBOOK:String = "1"
        val SOCIAL_TYPE_GOOGLE:String = "7"
        val SOCIAL_TYPE_SEAMLESS:String = "12"

        fun generateParamLoginEmail(email: String, password: String):
                Map<String, Any> {
            val requestParams = HashMap<String, Any>()

            requestParams[PARAM_USERNAME] = TokenGenerator().encode(email)
            requestParams[PARAM_PASSWORD] = TokenGenerator().encode(password)
            requestParams[PARAM_GRANT_TYPE] = TokenGenerator().encode(TYPE_PASSWORD)
            requestParams[PARAM_SUPPORTED] = "true"

            return requestParams
        }

        fun generateParamLoginAfterSQ(userSession: UserSessionInterface, validateToken: String): Map<String, Any> {
            val requestParams = HashMap<String, Any>()

            requestParams[PARAM_VALIDATE_TOKEN] = validateToken
            requestParams[PARAM_PASSWORD_TYPE] = TYPE_OTP
            requestParams[PARAM_ACCESS_TOKEN] = userSession.accessToken
            requestParams[PARAM_GRANT_TYPE] = TokenGenerator().encode(TYPE_PASSWORD)
            requestParams[PARAM_SUPPORTED] = "true"

            return requestParams
        }

        fun generateParamSocialMedia(accessToken: String, socialType : String): Map<String, Any> {
            val requestParams = HashMap<String, Any>()

            requestParams[PARAM_SOCIAL_TYPE] = socialType
            requestParams[PARAM_ACCESS_TOKEN] = accessToken
            requestParams[PARAM_GRANT_TYPE] = TokenGenerator().encode(TYPE_EXTENSION)
            requestParams[PARAM_SUPPORTED] = "true"

            return requestParams
        }

        fun generateParamForFingerprint(validateToken: String, userId: String): Map<String, Any> {
            val requestParams = HashMap<String, Any>()

            requestParams[PARAM_GRANT_TYPE] = TokenGenerator().encode(TYPE_PASSWORD)
            requestParams[PARAM_PASSWORD_TYPE] = TYPE_FINGERPRINT
            requestParams[PARAM_USERNAME] = TokenGenerator().encode(userId)
            requestParams[PARAM_PASSWORD] = TokenGenerator().encode(validateToken)

            return requestParams
        }

        fun generateParamSocialMediaPhone(accessToken: String, email: String, socialType : String): Map<String, Any> {
            val requestParams = HashMap<String, Any>()

            requestParams[PARAM_USERNAME] = TokenGenerator().encode(email)
            requestParams[PARAM_SOCIAL_TYPE] = socialType
            requestParams[PARAM_VALIDATE_TOKEN] = accessToken
            requestParams[PARAM_GRANT_TYPE] = TokenGenerator().encode(TYPE_EXTENSION)
            requestParams[PARAM_SUPPORTED] = "true"

            return requestParams
        }

        fun generateParamLoginPhone(key: String, email: String, phoneNumber: String): Map<String, Any> {
            val requestParams = HashMap<String, Any>()

            requestParams[PARAM_USERNAME] = TokenGenerator().encode(email)
            requestParams[PARAM_PASSWORD] = TokenGenerator().encode(key)
            requestParams[PARAM_CODE] = phoneNumber
            requestParams[PARAM_GRANT_TYPE] = TokenGenerator().encode(TYPE_PASSWORD)
            requestParams[PARAM_PASSWORD_TYPE] = TYPE_LPN
            requestParams[PARAM_SUPPORTED] = "true"

            return requestParams
        }

        fun generateParamLoginSeamless(code: String):
                Map<String, Any> {
            val requestParams = HashMap<String, Any>()

            requestParams[PARAM_SOCIAL_TYPE] = SOCIAL_TYPE_SEAMLESS
            requestParams[PARAM_ACCESS_TOKEN] = code
            requestParams[PARAM_GRANT_TYPE] = TokenGenerator().encode(TYPE_EXTENSION)

            return requestParams
        }

    }

    fun unsubscribe() {
        graphqlUseCase.unsubscribe()
    }


}
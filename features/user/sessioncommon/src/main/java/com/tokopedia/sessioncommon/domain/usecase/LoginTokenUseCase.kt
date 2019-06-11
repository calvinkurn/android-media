package com.tokopedia.sessioncommon.domain.usecase

import android.content.res.Resources
import com.google.gson.JsonObject
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.sessioncommon.R
import com.tokopedia.sessioncommon.data.register.LoginTokenPojo
import com.tokopedia.sessioncommon.di.SessionModule
import com.tokopedia.sessioncommon.util.TokenGenerator
import com.tokopedia.user.session.UserSessionInterface
import rx.Subscriber
import javax.inject.Inject
import javax.inject.Named

/**
 * @author by nisie on 28/05/19.
 */

class LoginTokenUseCase @Inject constructor(val resources: Resources,
                                            private val graphqlUseCase: GraphqlUseCase,
                                            @Named(SessionModule.SESSION_MODULE)
                                            private val userSession: UserSessionInterface
) {

    fun executeLoginEmailWithPassword(requestParams: Map<String, Any>, subscriber:
    Subscriber<GraphqlResponse>) {
        val query = GraphqlHelper.loadRawString(resources, R.raw.mutation_login_email)
        val graphqlRequest = GraphqlRequest(query,
                LoginTokenPojo::class.java, requestParams)

        userSession.setToken(TokenGenerator().createBasicTokenGQL(), TokenGenerator.TOKEN_TYPE)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        graphqlUseCase.execute(subscriber)
    }


    fun execute(requestParams: Map<String, Any>, subscriber: Subscriber<GraphqlResponse>) {
        val query = GraphqlHelper.loadRawString(resources, R.raw.mutation_login_email)
        val graphqlRequest = GraphqlRequest(query,
                LoginTokenPojo::class.java, requestParams)

        userSession.setToken(TokenGenerator().createBasicTokenGQL(), TokenGenerator.TOKEN_TYPE)
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        graphqlUseCase.execute(subscriber)
    }

    companion object {

        private val PARAM_INPUT: String = "input"
        private val PARAM_GRANT_TYPE: String = "grant_type"
        private val PARAM_PASSWORD_TYPE: String = "password_type"
        private val PARAM_SUPPORTED: String = "supported"

        private val PARAM_USERNAME: String = "username"
        private val PARAM_PASSWORD: String = "password"
        private val PARAM_ACCESS_TOKEN: String = "access_token"
        private val PARAM_REFRESH_TOKEN: String = "refresh_token"

        private val TYPE_PASSWORD:String = "password"

        fun generateParamLoginEmail(email: String, password: String):
                Map<String, Any> {
            val requestParams = HashMap<String, Any>()

            requestParams[PARAM_USERNAME] = TokenGenerator().encode(email)
            requestParams[PARAM_PASSWORD] = TokenGenerator().encode(password)
            requestParams[PARAM_GRANT_TYPE] = TokenGenerator().encode(TYPE_PASSWORD)
            requestParams[PARAM_PASSWORD_TYPE] = TYPE_PASSWORD
            requestParams[PARAM_SUPPORTED] = "true"

            return requestParams
        }
    }

    fun unsubscribe() {
        graphqlUseCase.unsubscribe()
    }
}
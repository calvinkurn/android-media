package com.tokopedia.sessioncommon.domain.usecase

import android.content.res.Resources
import com.google.gson.JsonObject
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.sessioncommon.R
import com.tokopedia.sessioncommon.data.register.RegisterPojo
import com.tokopedia.sessioncommon.util.TokenGenerator
import com.tokopedia.user.session.UserSessionInterface
import rx.Subscriber
import javax.inject.Inject

/**
 * @author by nisie on 30/04/19.
 */
class RegisterUseCase @Inject constructor(val resources: Resources,
                                          private val graphqlUseCase: GraphqlUseCase,
                                          private val userSession: UserSessionInterface
) {
    fun execute(requestParams: Map<String, Any>, subscriber: Subscriber<GraphqlResponse>) {
        val query = GraphqlHelper.loadRawString(resources, R.raw.mutation_register)
        val graphqlRequest = GraphqlRequest(query,
                RegisterPojo::class.java, requestParams)

        userSession.setToken(TokenGenerator().createBasicTokenGQL(),"")
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        graphqlUseCase.execute(subscriber)
    }

    companion object {

        private val PARAM_INPUT: String = "input"
        private val PARAM_PHONE_NUMBER: String = "phone"
        private val PARAM_REG_TYPE: String = "reg_type"
        private val PARAM_FULL_NAME: String = "fullname"
        private val PARAM_EMAIL: String = "email"
        private val PARAM_PASSWORD: String = "password"
        private val PARAM_OS_TYPE: String = "os_type"

        private val OS_TYPE_ANDROID: String = "1"
        private val REG_TYPE_PHONE: String = "phone"


        fun generateParamRegisterPhone(name: String, phoneNumber: String):
                Map<String, Any> {
            val requestParams = HashMap<String, Any>()

            val input = JsonObject()
            input.addProperty(PARAM_PHONE_NUMBER, phoneNumber)
            input.addProperty(PARAM_FULL_NAME, name)
            input.addProperty(PARAM_OS_TYPE, OS_TYPE_ANDROID)
            input.addProperty(PARAM_REG_TYPE, REG_TYPE_PHONE)

            requestParams[PARAM_INPUT] = input
            return requestParams
        }
    }

    fun unsubscribe() {
        graphqlUseCase.unsubscribe()
    }
}
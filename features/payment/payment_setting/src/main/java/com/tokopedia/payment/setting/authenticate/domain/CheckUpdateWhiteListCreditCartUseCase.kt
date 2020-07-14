package com.tokopedia.payment.setting.authenticate.domain

import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.payment.setting.authenticate.di.GQL_CHECK_UPDATE_WHITE_LIST
import com.tokopedia.payment.setting.authenticate.model.CheckWhiteListResponse
import rx.Subscriber
import javax.inject.Inject
import javax.inject.Named

class CheckUpdateWhiteListCreditCartUseCase @Inject constructor(
        @Named(GQL_CHECK_UPDATE_WHITE_LIST) val query: String,
        private val graphqlUseCase: GraphqlUseCase) {

    fun execute(authValue: Int, status: Boolean,
                token: String?, subscriber: Subscriber<GraphqlResponse>) {
        graphqlUseCase.clearRequest()
        val params = mapOf<String, Any?>(
                UPDATE_STATUS to status,
                AUTH_VALUE to authValue,
                TOKEN to token)
        val graphqlRequestForUsable = GraphqlRequest(query,
                CheckWhiteListResponse::class.java, params)
        graphqlUseCase.addRequest(graphqlRequestForUsable)
        graphqlUseCase.execute(subscriber)
    }

    fun unSubscribe() {
        graphqlUseCase.unsubscribe()
    }

    companion object {
        val UPDATE_STATUS = "updateStatus"
        val AUTH_VALUE = "authValue"
        val TOKEN = "token"
    }

}
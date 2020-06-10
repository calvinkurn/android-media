package com.tokopedia.payment.setting.detail.domain

import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.payment.setting.detail.di.GQL_DELETE_CREDIT_CARD_LIST
import com.tokopedia.payment.setting.detail.model.DataResponseDeleteCC
import rx.Subscriber
import javax.inject.Inject
import javax.inject.Named

class GQLDeleteCreditCardQueryUseCase @Inject constructor(
        @Named(GQL_DELETE_CREDIT_CARD_LIST) val query: String,
        private val graphqlUseCase: GraphqlUseCase) {

    fun execute(token: String, subscriber: Subscriber<GraphqlResponse>) {
        graphqlUseCase.unsubscribe()
        graphqlUseCase.clearRequest()
        val params = mapOf<String, Any>(TOKEN to token)
        val graphqlRequestForUsable = GraphqlRequest(query, DataResponseDeleteCC::class.java, params)
        graphqlUseCase.addRequest(graphqlRequestForUsable)
        graphqlUseCase.execute(subscriber)
    }

    companion object {
        const val TOKEN = "token_id"
    }


}
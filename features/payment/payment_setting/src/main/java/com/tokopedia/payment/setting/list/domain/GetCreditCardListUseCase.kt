package com.tokopedia.payment.setting.list.domain

import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.payment.setting.list.di.GQL_GET_CREDIT_CARD_LIST
import com.tokopedia.payment.setting.list.model.GQLPaymentQueryResponse
import rx.Subscriber
import javax.inject.Inject
import javax.inject.Named

class GetCreditCardListUseCase @Inject constructor(
        @Named(GQL_GET_CREDIT_CARD_LIST) val query: String,
        private val graphqlUseCase: GraphqlUseCase) {

    fun execute(subscriber: Subscriber<GraphqlResponse>) {
        unSubscribe()
        graphqlUseCase.clearRequest()
        val graphqlRequestForUsable = GraphqlRequest(query, GQLPaymentQueryResponse::class.java)
        graphqlUseCase.addRequest(graphqlRequestForUsable)
        graphqlUseCase.execute(subscriber)
    }

    fun unSubscribe() {
        graphqlUseCase.unsubscribe()
    }

}
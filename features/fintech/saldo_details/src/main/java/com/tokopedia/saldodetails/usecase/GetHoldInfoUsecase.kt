package com.tokopedia.saldodetails.usecase

import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.saldodetails.di.GqlQueryModule
import com.tokopedia.saldodetails.di.SaldoDetailsScope
import com.tokopedia.saldodetails.response.model.saldoholdinfo.response.SaldoHoldResponse
import rx.Subscriber
import java.util.*
import javax.inject.Inject
import javax.inject.Named

@SaldoDetailsScope
class GetHoldInfoUsecase @Inject constructor(@Named(GqlQueryModule.QUERY_SALDO_HOLD_INFO) val query: String, val graphqlUseCase: GraphqlUseCase) {

    fun unsubscribe() {
        graphqlUseCase.unsubscribe()
    }

    fun execute(subscriber: Subscriber<GraphqlResponse>) {
        graphqlUseCase.clearRequest()
        val variables = HashMap<String, Any>()

        val graphqlRequest = GraphqlRequest(
                query,
                SaldoHoldResponse::class.java,
                variables, false)

        graphqlUseCase.addRequest(graphqlRequest)
        graphqlUseCase.execute(subscriber)
    }
}


package com.tokopedia.saldodetails.usecase

import android.content.res.Resources
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.saldodetails.R
import com.tokopedia.saldodetails.response.model.saldoholdinfo.response.SaldoHoldResponse
import java.util.HashMap
import rx.Subscriber

class GetHoldInfoUsecase (val resources: Resources, val graphqlUseCase: GraphqlUseCase) {

    fun unsubscribe() {
        graphqlUseCase.unsubscribe()
    }

    fun execute(subscriber: Subscriber<GraphqlResponse>) {
        graphqlUseCase.clearRequest()
        val variables = HashMap<String, Any>()

        val graphqlRequest = GraphqlRequest(
                GraphqlHelper.loadRawString(resources, R.raw.query_saldo_hold_info),
                SaldoHoldResponse::class.java,
                variables, false)

        graphqlUseCase.addRequest(graphqlRequest)
        graphqlUseCase.execute(subscriber)
    }
}


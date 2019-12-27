package com.tokopedia.saldodetails.usecase

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.saldodetails.response.model.GqlMerchantSaldoDetailsResponse
import rx.Subscriber
import java.util.*
import javax.inject.Inject

class GetMerchantSaldoDetails @Inject
constructor(@ApplicationContext val context: Context) {

    private val graphqlUseCase = GraphqlUseCase()

    fun unsubscribe() {
        graphqlUseCase.unsubscribe()
    }

    fun execute(subscriber: Subscriber<GraphqlResponse>) {
        graphqlUseCase.clearRequest()
        val variables = HashMap<String, Any>()

        val graphqlRequest = GraphqlRequest(
                GraphqlHelper.loadRawString(context.resources,
                        com.tokopedia.saldodetails.R.raw.query_get_merchant_saldo_details),
                GqlMerchantSaldoDetailsResponse::class.java,
                variables, false)

        graphqlUseCase.addRequest(graphqlRequest)
        graphqlUseCase.execute(subscriber)
    }
}

package com.tokopedia.saldodetails.usecase

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.saldodetails.response.model.GqlMerchantCreditDetailsResponse
import rx.Subscriber
import java.util.*
import javax.inject.Inject

class GetMerchantCreditDetails @Inject
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
                        com.tokopedia.saldodetails.R.raw.query_get_merchant_credit_details),
                GqlMerchantCreditDetailsResponse::class.java,
                variables, false)

        graphqlUseCase.addRequest(graphqlRequest)
        graphqlUseCase.execute(subscriber)
    }
}

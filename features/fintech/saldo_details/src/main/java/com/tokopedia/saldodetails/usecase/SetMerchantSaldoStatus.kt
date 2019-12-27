package com.tokopedia.saldodetails.usecase

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.saldodetails.response.model.GqlSetMerchantSaldoStatus
import rx.Subscriber
import java.util.*
import javax.inject.Inject

class SetMerchantSaldoStatus @Inject
constructor(@ApplicationContext var context: Context) {
    private val graphqlUseCase = GraphqlUseCase()

    fun unsubscribe() {
        graphqlUseCase.unsubscribe()
    }

    fun execute(isEnabled: Boolean, subscriber: Subscriber<GraphqlResponse>) {
        graphqlUseCase.clearRequest()
        val variables = HashMap<String, Any>()
        variables["enable"] = isEnabled

        val graphqlRequest = GraphqlRequest(
                GraphqlHelper.loadRawString(context.resources, com.tokopedia.saldodetails.R.raw.query_set_saldo_status),
                GqlSetMerchantSaldoStatus::class.java,
                variables, SET_MERCHANT_SALDO_STATUS, false)

        graphqlUseCase.addRequest(graphqlRequest)
        graphqlUseCase.execute(subscriber)
    }

    companion object {
        private val SET_MERCHANT_SALDO_STATUS = ""
    }
}

package com.tokopedia.saldodetails.usecase

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.saldodetails.response.model.GqlCompleteTransactionResponse
import rx.Subscriber
import javax.inject.Inject

class GetAllTransactionUsecase @Inject
constructor(@ApplicationContext val context: Context) {
    private val graphqlUseCase = GraphqlUseCase()
    private var isRequesting: Boolean = false
    private var variables: Map<String, Any>? = null

    // TODO: 27/12/19 remove context from usecase

    fun unsubscribe() {
        graphqlUseCase.unsubscribe()
    }

    fun setRequestVariables(variables: Map<String, Any>) {
        this.variables = variables
    }

    fun execute(subscriber: Subscriber<GraphqlResponse>) {
        graphqlUseCase.clearRequest()
        setRequesting(true)
        val query = GraphqlHelper.loadRawString(context.resources,
                com.tokopedia.saldodetails.R.raw.query_deposit_all_transaction)

        val graphqlRequest = GraphqlRequest(query, GqlCompleteTransactionResponse::class.java,
                variables, GET_SUMMARY_DEPOSIT, false)

        graphqlUseCase.addRequest(graphqlRequest)
        graphqlUseCase.execute(subscriber)
    }

    fun setRequesting(isRequesting: Boolean) {
        this.isRequesting = isRequesting
    }

    companion object {
        private val GET_SUMMARY_DEPOSIT = "DepositActivityQuery"
    }
}

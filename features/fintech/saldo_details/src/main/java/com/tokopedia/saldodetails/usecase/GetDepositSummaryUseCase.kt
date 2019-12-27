package com.tokopedia.saldodetails.usecase

import android.content.Context

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.saldodetails.response.model.GqlAllDepositSummaryResponse

import javax.inject.Inject

import rx.Subscriber

class GetDepositSummaryUseCase @Inject
constructor(@ApplicationContext val context: Context) {
    private val graphqlUseCase = GraphqlUseCase()
    var isRequesting: Boolean = false
    private var variables: Map<String, Any>? = null

    fun unsubscribe() {
        graphqlUseCase.unsubscribe()
    }

    fun setRequestVariables(variables: Map<String, Any>) {
        this.variables = variables
    }

    fun execute(subscriber: Subscriber<GraphqlResponse>) {
        graphqlUseCase.clearRequest()
        isRequesting = true

        val query = GraphqlHelper.loadRawString(context.resources,
                com.tokopedia.saldodetails.R.raw.query_deposit_details_for_all)
        val graphqlRequest = GraphqlRequest(query, GqlAllDepositSummaryResponse::class.java,
                variables, GET_SUMMARY_DEPOSIT, false)

        graphqlUseCase.addRequest(graphqlRequest)
        graphqlUseCase.execute(subscriber)
    }

    companion object {
        private val GET_SUMMARY_DEPOSIT = "DepositActivityQuery"
    }
}

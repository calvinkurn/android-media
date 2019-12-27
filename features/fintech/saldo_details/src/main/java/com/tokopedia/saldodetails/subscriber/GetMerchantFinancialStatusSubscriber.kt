package com.tokopedia.saldodetails.subscriber

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.saldodetails.deposit.listener.MerchantFinancialStatusActionListener
import com.tokopedia.saldodetails.response.model.GqlMerchantCreditDetailsResponse
import com.tokopedia.saldodetails.response.model.GqlMerchantSaldoDetailsResponse

import rx.Subscriber

class GetMerchantFinancialStatusSubscriber(
        private val viewListener: MerchantFinancialStatusActionListener) : Subscriber<GraphqlResponse>() {

    override fun onCompleted() {

    }

    override fun onError(e: Throwable) {
        viewListener.hideUserFinancialStatusLayout()
    }

    override fun onNext(graphqlResponse: GraphqlResponse?) {

        if (graphqlResponse?.getData<Any>(GqlMerchantSaldoDetailsResponse::class.java) != null) {

            val gqlMerchantSaldoDetailsResponse = graphqlResponse.getData<GqlMerchantSaldoDetailsResponse>(GqlMerchantSaldoDetailsResponse::class.java)

            viewListener.showSaldoPrioritasFragment(gqlMerchantSaldoDetailsResponse.data)

        } else {
            viewListener.hideSaldoPrioritasFragment()
        }

        if (graphqlResponse?.getData<Any>(GqlMerchantCreditDetailsResponse::class.java) != null) {

            val response = graphqlResponse.getData<GqlMerchantCreditDetailsResponse>(GqlMerchantCreditDetailsResponse::class.java)
            viewListener.showMerchantCreditLineFragment(response.data)

        } else {
            viewListener.hideMerchantCreditLineFragment()
        }
    }
}

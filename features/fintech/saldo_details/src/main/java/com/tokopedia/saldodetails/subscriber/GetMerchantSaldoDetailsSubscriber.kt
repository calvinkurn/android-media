package com.tokopedia.saldodetails.subscriber

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.saldodetails.deposit.listener.MerchantFinancialStatusActionListener
import com.tokopedia.saldodetails.response.model.GqlMerchantSaldoDetailsResponse

import rx.Subscriber

class GetMerchantSaldoDetailsSubscriber(
        private val viewListener: MerchantFinancialStatusActionListener) : Subscriber<GraphqlResponse>() {

    override fun onCompleted() {

    }

    override fun onError(e: Throwable) {
        viewListener.hideSaldoPrioritasFragment()
    }

    override fun onNext(graphqlResponse: GraphqlResponse?) {
        if (graphqlResponse?.getData<Any>(GqlMerchantSaldoDetailsResponse::class.java) != null) {
            val gqlMerchantSaldoDetailsResponse =
                    graphqlResponse.getData<GqlMerchantSaldoDetailsResponse>(GqlMerchantSaldoDetailsResponse::class.java)
            viewListener.showSaldoPrioritasFragment(gqlMerchantSaldoDetailsResponse.data)

        } else {
            viewListener.hideSaldoPrioritasFragment()
        }
    }
}

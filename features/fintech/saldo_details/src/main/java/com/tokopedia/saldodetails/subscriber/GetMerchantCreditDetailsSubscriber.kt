package com.tokopedia.saldodetails.subscriber

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.saldodetails.deposit.listener.MerchantFinancialStatusActionListener
import com.tokopedia.saldodetails.response.model.GqlMerchantCreditDetailsResponse

import rx.Subscriber

class GetMerchantCreditDetailsSubscriber(
        private val viewListener: MerchantFinancialStatusActionListener): Subscriber<GraphqlResponse>() {

    override fun onCompleted() {

    }

    override fun onError(e: Throwable) {
        viewListener.hideMerchantCreditLineFragment()
    }

    override fun onNext(graphqlResponse: GraphqlResponse?) {

        if (graphqlResponse != null && graphqlResponse.getData<Any>(GqlMerchantCreditDetailsResponse::class.java) != null) {

            val response = graphqlResponse.
                    getData<GqlMerchantCreditDetailsResponse>(GqlMerchantCreditDetailsResponse::class.java)
            viewListener.showMerchantCreditLineFragment(response.data)

        } else {
            viewListener.hideMerchantCreditLineFragment()
        }
    }
}

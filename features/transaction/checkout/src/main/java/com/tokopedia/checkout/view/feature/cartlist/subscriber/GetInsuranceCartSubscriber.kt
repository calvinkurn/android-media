package com.tokopedia.checkout.view.feature.cartlist.subscriber

import com.tokopedia.checkout.view.feature.cartlist.ICartListView
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.transactiondata.insurance.entity.response.InsuranceCartGqlResponse
import rx.Subscriber


class GetInsuranceCartSubscriber(val view: ICartListView) : Subscriber<GraphqlResponse>() {

    override fun onCompleted() {
    }

    override fun onError(e: Throwable?) {
        view.renderInsuranceCartData(null, false)
    }

    override fun onNext(graphqlResponse: GraphqlResponse?) {
        var insuranceCartGqlResponse: InsuranceCartGqlResponse? = null
        if (graphqlResponse?.getData<InsuranceCartGqlResponse>(InsuranceCartGqlResponse::class.java) != null) {
            insuranceCartGqlResponse = graphqlResponse.getData(InsuranceCartGqlResponse::class.java)
            view.renderInsuranceCartData(insuranceCartGqlResponse?.data, false)
        } else {
            view.renderInsuranceCartData(null, false)
        }
    }
}
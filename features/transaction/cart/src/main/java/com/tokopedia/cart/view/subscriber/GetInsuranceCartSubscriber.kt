package com.tokopedia.cart.view.subscriber

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.purchase_platform.common.feature.insurance.response.InsuranceCartGqlResponse
import com.tokopedia.cart.view.ICartListView
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
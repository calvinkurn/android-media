package com.tokopedia.cart.view.subscriber

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.purchase_platform.common.feature.insurance.response.RemoveInsuranceProductGqlResponse
import com.tokopedia.cart.view.ICartListView
import rx.Subscriber

class GetRemoveMacroInsuranceProductSubscriber(val view: ICartListView,
                                               val productId: List<Long>,
                                               val showToaster: Boolean) : Subscriber<GraphqlResponse>() {

    override fun onError(e: Throwable) {
        view.hideProgressLoading()
        view.showToastMessageRed(e)
    }

    override fun onCompleted() {

    }

    override fun onNext(graphqlResponse: GraphqlResponse?) {
        val removeInsuranceProductGqlResponse: RemoveInsuranceProductGqlResponse?
        if (graphqlResponse?.getData<RemoveInsuranceProductGqlResponse>(RemoveInsuranceProductGqlResponse::class.java) != null) {
            removeInsuranceProductGqlResponse = graphqlResponse.getData(RemoveInsuranceProductGqlResponse::class.java)

            if (removeInsuranceProductGqlResponse?.response?.removeTransactional?.status!!) {
                if (showToaster) {
                    view.showMessageRemoveInsuranceProductSuccess()
                }
                view.removeInsuranceProductItem(productId)
            } else {
                view.showToastMessageRed(
                        removeInsuranceProductGqlResponse.response.removeTransactional.errorMessage)
            }
        }
        view.hideProgressLoading()
    }

}
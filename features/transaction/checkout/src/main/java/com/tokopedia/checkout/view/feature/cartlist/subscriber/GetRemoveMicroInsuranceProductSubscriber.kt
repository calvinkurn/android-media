package com.tokopedia.checkout.view.feature.cartlist.subscriber

import com.tokopedia.checkout.view.feature.cartlist.ICartListView
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.transactiondata.insurance.entity.response.RemoveInsuranceProductGqlResponse
import rx.Subscriber

class GetRemoveMicroInsuranceProductSubscriber(val view: ICartListView,
                                               val productId: List<Long>,
                                               val showToaster: Boolean) : Subscriber<GraphqlResponse>() {


    override fun onCompleted() {


    }

    override fun onError(e: Throwable?) {
        view.hideProgressLoading()
        view.showToastMessageRed(ErrorHandler.getErrorMessage(view.activity, e))

    }

    override fun onNext(graphqlResponse: GraphqlResponse?) {
        val removeInsuranceProductGqlResponse: RemoveInsuranceProductGqlResponse?
        if (graphqlResponse != null &&
                graphqlResponse.getData<RemoveInsuranceProductGqlResponse>(RemoveInsuranceProductGqlResponse::class.java) != null) {

            removeInsuranceProductGqlResponse = graphqlResponse.getData(RemoveInsuranceProductGqlResponse::class.java)
            if (removeInsuranceProductGqlResponse!!.response.removeTransactional.status) {
                view.removeInsuranceProductItem(productId)
            }
        }
        view.hideProgressLoading()

    }
}
package com.tokopedia.checkout.view.feature.cartlist.subscriber

import com.tokopedia.checkout.R
import com.tokopedia.checkout.view.feature.cartlist.ICartListView
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.transactiondata.insurance.entity.response.RemoveInsuranceProductGqlResponse
import rx.Subscriber

class GetRemoveMacroInsuranceProductSubscriber(val view: ICartListView,
                                               val productId: List<Long>,
                                               val showToaster: Boolean) : Subscriber<GraphqlResponse>() {

    override fun onError(e: Throwable?) {
        view.hideProgressLoading()
        view.showToastMessageRed(ErrorHandler.getErrorMessage(view.activity, e))
    }

    override fun onCompleted() {

    }

    override fun onNext(graphqlResponse: GraphqlResponse?) {
        val removeInsuranceProductGqlResponse: RemoveInsuranceProductGqlResponse?
        if (graphqlResponse?.getData<RemoveInsuranceProductGqlResponse>(RemoveInsuranceProductGqlResponse::class.java) != null) {
            removeInsuranceProductGqlResponse = graphqlResponse.getData(RemoveInsuranceProductGqlResponse::class.java)

            if (removeInsuranceProductGqlResponse?.response?.removeTransactional?.status!!) {
                if (showToaster) {
                    view.showToastMessageGreen(view.activity.resources.getString(R.string.remove_macro_insurance_success))
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
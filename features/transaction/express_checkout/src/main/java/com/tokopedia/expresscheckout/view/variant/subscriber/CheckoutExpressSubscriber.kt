package com.tokopedia.expresscheckout.view.variant.subscriber

import com.tokopedia.expresscheckout.data.entity.response.checkout.CheckoutExpressGqlResponse
import com.tokopedia.expresscheckout.view.variant.CheckoutVariantContract
import com.tokopedia.graphql.data.model.GraphqlResponse
import rx.Subscriber

/**
 * Created by Irfan Khoirul on 11/01/19.
 */

class CheckoutExpressSubscriber(val view: CheckoutVariantContract.View?, val presenter: CheckoutVariantContract.Presenter) :
        Subscriber<GraphqlResponse>() {

    override fun onCompleted() {

    }

    override fun onError(e: Throwable) {
        e.printStackTrace()
        view?.hideLoadingDialog()
    }

    override fun onNext(response: GraphqlResponse) {
        view?.hideLoadingDialog()
        val expressCheckoutResponse = response.getData<CheckoutExpressGqlResponse>(CheckoutExpressGqlResponse::class.java)
        if (expressCheckoutResponse.checkoutResponse.header.errorCode.equals("200")) {
            // Todo : Navigate to thank you page
        } else {
            // Todo : show bottomsheet error
            view?.showBottomsheetError("Checkout error", "Checkout error", "Checkout error")
        }
    }

}
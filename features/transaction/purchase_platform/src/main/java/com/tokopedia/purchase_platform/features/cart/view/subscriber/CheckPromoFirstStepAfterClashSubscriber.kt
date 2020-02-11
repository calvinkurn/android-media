package com.tokopedia.purchase_platform.features.cart.view.subscriber

import com.tokopedia.promocheckout.common.util.mapToStatePromoStackingCheckout
import com.tokopedia.promocheckout.common.view.uimodel.ResponseGetPromoStackUiModel
import com.tokopedia.promocheckout.common.view.widget.TickerPromoStackingCheckoutView
import com.tokopedia.purchase_platform.features.cart.view.ICartListView
import rx.Subscriber

/**
 * Created by Irfan Khoirul on 25/03/19.
 */

class CheckPromoFirstStepAfterClashSubscriber(val view: ICartListView?,
                                              val type: String) : Subscriber<ResponseGetPromoStackUiModel>() {
    private val STATUS_OK = "OK"

    override fun onCompleted() {

    }

    override fun onError(e: Throwable) {
        e.printStackTrace()
        view?.hideProgressLoading()
        view?.showToastMessageRed(e)
    }

    override fun onNext(responseGetPromoStack: ResponseGetPromoStackUiModel) {
        view?.hideProgressLoading()

        if (responseGetPromoStack.status.equals(STATUS_OK, true)) {
            if (responseGetPromoStack.data.clashings.isClashedPromos) {
                view?.onClashCheckPromo(responseGetPromoStack.data.clashings, type)
            } else {
                var isRed = false
                var message = ""
                if (responseGetPromoStack.data.message.state.mapToStatePromoStackingCheckout() == TickerPromoStackingCheckoutView.State.FAILED) {
                    isRed = true
                    message = responseGetPromoStack.data.message.text
                } else {
                    responseGetPromoStack.data.voucherOrders.forEach {
                        if (it.message.state.mapToStatePromoStackingCheckout() == TickerPromoStackingCheckoutView.State.FAILED) {
                            isRed = true
                            message = it.message.text
                        }
                    }
                }

                if (isRed) {
                    view?.hideProgressLoading()
                    view?.showToastMessageRed(message)
                } else {
                    view?.onSuccessCheckPromoFirstStep(responseGetPromoStack)
                }
            }
        } else {
            val message = responseGetPromoStack.data.message.text
            view?.showToastMessageRed(message)
        }
    }

}
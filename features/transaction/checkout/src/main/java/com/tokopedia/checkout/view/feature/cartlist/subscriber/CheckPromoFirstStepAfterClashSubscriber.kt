package com.tokopedia.checkout.view.feature.cartlist.subscriber

import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.checkout.view.feature.cartlist.ICartListPresenter
import com.tokopedia.checkout.view.feature.cartlist.ICartListView
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.promocheckout.common.domain.mapper.CheckPromoStackingCodeMapper
import com.tokopedia.promocheckout.common.util.mapToStatePromoStackingCheckout
import com.tokopedia.promocheckout.common.view.widget.TickerPromoStackingCheckoutView
import rx.Subscriber

/**
 * Created by Irfan Khoirul on 25/03/19.
 */

class CheckPromoFirstStepAfterClashSubscriber(val view: ICartListView?,
                                              val presenter: ICartListPresenter,
                                              val checkPromoCodeStackingCodeMapper: CheckPromoStackingCodeMapper,
                                              val type: String)
    : Subscriber<GraphqlResponse>() {

    private val statusOK = "OK"

    override fun onCompleted() {

    }

    override fun onError(e: Throwable) {
        e.printStackTrace()
        view?.hideProgressLoading()
        view?.showToastMessageRed(ErrorHandler.getErrorMessage(view.activity, e))
    }

    override fun onNext(response: GraphqlResponse) {
        view?.hideProgressLoading()
        val responseGetPromoStack = checkPromoCodeStackingCodeMapper.call(response)

        if (responseGetPromoStack.status.equals(statusOK, true)) {
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
package com.tokopedia.purchase_platform.features.checkout.view.subscriber

import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.purchase_platform.features.checkout.view.ShipmentContract
import com.tokopedia.purchase_platform.features.checkout.view.ShipmentPresenter
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.promocheckout.common.domain.mapper.CheckPromoStackingCodeMapper
import com.tokopedia.promocheckout.common.util.mapToStatePromoStackingCheckout
import com.tokopedia.promocheckout.common.view.widget.TickerPromoStackingCheckoutView
import rx.Subscriber


class CheckShipmentPromoFirstStepAfterClashSubscriber(private val view: ShipmentContract.View?,
                                                      private val presenter: ShipmentPresenter,
                                                      private val checkPromoStackingCodeMapper: CheckPromoStackingCodeMapper,
                                                      private val type: String,
                                                      private val promoCode: String) : Subscriber<GraphqlResponse>() {

    override fun onCompleted() {

    }

    override fun onError(e: Throwable) {
        e.printStackTrace()
        view?.hideLoading()
        view?.showToastError(ErrorHandler.getErrorMessage(view.activityContext, e))
    }

    override fun onNext(response: GraphqlResponse) {
        view?.hideLoading()
        presenter.couponStateChanged = true
        checkPromoStackingCodeMapper.isFinal = false
        val responseGetPromoStack = checkPromoStackingCodeMapper.call(response)
        if (responseGetPromoStack.status != "OK" || responseGetPromoStack.data.message.state.mapToStatePromoStackingCheckout() == TickerPromoStackingCheckoutView.State.FAILED) {
            val message = responseGetPromoStack.data.message.text
            view?.showToastError(message)
        } else {
            if (responseGetPromoStack.data.clashings.isClashedPromos) {
                view?.onClashCheckPromo(responseGetPromoStack.data.clashings, type)
            } else {
                view?.onSuccessCheckPromoFirstStepAfterClash(responseGetPromoStack, promoCode)
            }
        }
    }

}
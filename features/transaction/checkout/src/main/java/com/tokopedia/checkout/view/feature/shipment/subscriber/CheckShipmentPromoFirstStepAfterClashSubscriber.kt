package com.tokopedia.checkout.view.feature.shipment.subscriber

import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.checkout.view.feature.shipment.ShipmentContract
import com.tokopedia.checkout.view.feature.shipment.ShipmentPresenter
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.promocheckout.common.domain.mapper.CheckPromoStackingCodeMapper
import com.tokopedia.promocheckout.common.util.mapToStatePromoStackingCheckout
import com.tokopedia.promocheckout.common.view.widget.TickerPromoStackingCheckoutView
import rx.Subscriber


class CheckShipmentPromoFirstStepAfterClashSubscriber(private val view: ShipmentContract.View?,
                                                      private val presenter: ShipmentPresenter,
                                                      private val checkPromoStackingCodeMapper: CheckPromoStackingCodeMapper,
                                                      private val isFromMultipleAddress: Boolean,
                                                      private val isOneClickShipment: Boolean,
                                                      private val cornerId: String,
                                                      private val isTradeIn: Boolean,
                                                      private val deviceId: String,
                                                      private val type: String) : Subscriber<GraphqlResponse>() {

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
                view?.onSuccessCheckPromoFirstStep(responseGetPromoStack)
            }
        }
    }

}
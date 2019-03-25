package com.tokopedia.checkout.view.feature.shipment.subscriber

import com.tokopedia.checkout.view.feature.shipment.ShipmentContract
import com.tokopedia.checkout.view.feature.shipment.ShipmentPresenter
import com.tokopedia.graphql.data.model.GraphqlResponse
import rx.Subscriber


class CheckShipmentPromoFirstStepAfterClashSubscriber(val view: ShipmentContract.View?,
                                                      val presenter: ShipmentPresenter,
                                                      private val promoListSize: Int,
                                                      private val isFromMultipleAddress: Boolean,
                                                      private val isOneClickShipment: Boolean,
                                                      private val cornerId: String,
                                                      private val currentPromoIndex: Int) : Subscriber<GraphqlResponse>() {

    override fun onCompleted() {

    }

    override fun onError(e: Throwable) {
        e.printStackTrace()
        if (currentPromoIndex == promoListSize - 1) {
            view?.hideLoading()
            presenter.processInitialLoadCheckoutPage(isFromMultipleAddress, isOneClickShipment, cornerId)
        }
    }

    override fun onNext(response: GraphqlResponse) {
        if (currentPromoIndex == promoListSize - 1) {
            view?.hideLoading()
            presenter.processInitialLoadCheckoutPage(isFromMultipleAddress, isOneClickShipment, cornerId)
        }
    }

}
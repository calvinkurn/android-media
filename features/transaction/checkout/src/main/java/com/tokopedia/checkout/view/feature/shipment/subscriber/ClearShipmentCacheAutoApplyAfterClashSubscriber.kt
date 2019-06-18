package com.tokopedia.checkout.view.feature.shipment.subscriber

import com.tokopedia.checkout.view.feature.shipment.ShipmentContract
import com.tokopedia.checkout.view.feature.shipment.ShipmentPresenter
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.promocheckout.common.domain.model.clearpromo.ClearCacheAutoApplyStackResponse
import com.tokopedia.promocheckout.common.view.uimodel.ClashingVoucherOrderUiModel
import rx.Subscriber


class ClearShipmentCacheAutoApplyAfterClashSubscriber(val view: ShipmentContract.View?,
                                                      val presenter: ShipmentPresenter,
                                                      private val newPromoList: ArrayList<ClashingVoucherOrderUiModel>,
                                                      private val isFromMultipleAddress: Boolean,
                                                      private val isOneClickShipment: Boolean,
                                                      private val cornerId: String,
                                                      private val isTradeIn: Boolean,
                                                      private val devieId: String,
                                                      private val type: String) : Subscriber<GraphqlResponse>() {

    override fun onCompleted() {

    }

    override fun onError(e: Throwable) {
        e.printStackTrace()
        view?.hideLoading()
        view?.onFailedClearPromoStack(false)
    }

    override fun onNext(response: GraphqlResponse) {
        view?.hideLoading()
        presenter.setCouponStateChanged(true)
        val responseData = response.getData<ClearCacheAutoApplyStackResponse>(ClearCacheAutoApplyStackResponse::class.java)
        if (responseData.successData.success) {
            view?.onSuccessClearPromoStackAfterClash()
            presenter.applyPromoStackAfterClash(newPromoList, isFromMultipleAddress, isOneClickShipment, isTradeIn, cornerId, devieId, type)
        } else {
            view?.onFailedClearPromoStack(false)
        }
    }

}
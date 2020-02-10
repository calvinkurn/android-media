package com.tokopedia.purchase_platform.features.checkout.view.subscriber

import com.tokopedia.promocheckout.common.domain.model.clearpromo.ClearCacheAutoApplyStackResponse
import com.tokopedia.promocheckout.common.view.uimodel.ClashingVoucherOrderUiModel
import com.tokopedia.purchase_platform.features.checkout.view.ShipmentContract
import com.tokopedia.purchase_platform.features.checkout.view.ShipmentPresenter
import rx.Subscriber


class ClearShipmentCacheAutoApplyAfterClashSubscriber(val view: ShipmentContract.View?,
                                                      val presenter: ShipmentPresenter,
                                                      private val newPromoList: ArrayList<ClashingVoucherOrderUiModel>,
                                                      private val isFromMultipleAddress: Boolean,
                                                      private val isOneClickShipment: Boolean,
                                                      private val cornerId: String,
                                                      private val isTradeIn: Boolean,
                                                      private val devieId: String,
                                                      private val type: String) : Subscriber<ClearCacheAutoApplyStackResponse>() {

    override fun onCompleted() {

    }

    override fun onError(e: Throwable) {
        e.printStackTrace()
        view?.hideLoading()
        view?.onFailedClearPromoStack(false)
    }

    override fun onNext(response: ClearCacheAutoApplyStackResponse) {
        view?.hideLoading()
        presenter.setCouponStateChanged(true)
        if (response.successData.success) {
            view?.onSuccessClearPromoStackAfterClash()
            presenter.applyPromoStackAfterClash(newPromoList, isFromMultipleAddress, isOneClickShipment, isTradeIn, cornerId, devieId, type)
        } else {
            view?.onFailedClearPromoStack(false)
        }
    }

}
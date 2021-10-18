package com.tokopedia.checkout.bundle.view.subscriber

import com.tokopedia.promocheckout.common.view.model.clearpromo.ClearPromoUiModel
import com.tokopedia.checkout.bundle.view.ShipmentContract
import com.tokopedia.checkout.bundle.view.ShipmentPresenter
import rx.Subscriber


class ClearShipmentCacheAutoApplyAfterClashSubscriber(val view: ShipmentContract.View?,
                                                      val presenter: ShipmentPresenter) : Subscriber<ClearPromoUiModel>() {

    override fun onCompleted() {

    }

    override fun onError(e: Throwable) {
        view?.hideLoading()
        view?.setHasRunningApiCall(false)
    }

    override fun onNext(response: ClearPromoUiModel) {
        view?.hideLoading()
        view?.setHasRunningApiCall(false)
        view?.showToastNormal("Ada perubahan pada promo yang kamu pakai")
    }

}
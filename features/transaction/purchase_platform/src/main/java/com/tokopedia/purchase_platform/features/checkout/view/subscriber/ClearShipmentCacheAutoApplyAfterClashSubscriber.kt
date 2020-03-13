package com.tokopedia.purchase_platform.features.checkout.view.subscriber

import com.tokopedia.promocheckout.common.domain.model.clearpromo.ClearCacheAutoApplyStackResponse
import com.tokopedia.purchase_platform.features.checkout.view.ShipmentContract
import com.tokopedia.purchase_platform.features.checkout.view.ShipmentPresenter
import rx.Subscriber


class ClearShipmentCacheAutoApplyAfterClashSubscriber(val view: ShipmentContract.View?,
                                                      val presenter: ShipmentPresenter) : Subscriber<ClearCacheAutoApplyStackResponse>() {

    override fun onCompleted() {

    }

    override fun onError(e: Throwable) {
        // Nothing to do
    }

    override fun onNext(response: ClearCacheAutoApplyStackResponse) {
        view?.showToastNormal("Ada perubahan pada promo yang kamu gunakan")
    }

}
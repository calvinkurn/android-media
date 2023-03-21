package com.tokopedia.checkout.view.subscriber

import com.tokopedia.checkout.view.ShipmentContract
import com.tokopedia.checkout.view.ShipmentPresenter
import com.tokopedia.purchase_platform.common.feature.promo.view.model.clearpromo.ClearPromoUiModel
import com.tokopedia.purchase_platform.common.feature.promonoteligible.NotEligiblePromoHolderdata
import rx.Subscriber

/**
 * Created by Irfan Khoirul on 2019-06-25.
 */

class ClearNotEligiblePromoSubscriber(
    val view: ShipmentContract.View?,
    val presenter: ShipmentPresenter,
    val notEligiblePromoHolderdata: ArrayList<NotEligiblePromoHolderdata>
) : Subscriber<ClearPromoUiModel>() {

    override fun onCompleted() {
    }

    override fun onError(e: Throwable) {
        e.printStackTrace()
        view?.removeIneligiblePromo(notEligiblePromoHolderdata)
    }

    override fun onNext(response: ClearPromoUiModel?) {
        if (response?.successDataModel?.tickerMessage?.isNotBlank() == true) {
            val ticker = presenter.tickerAnnouncementHolderData.value
            ticker.title = ""
            ticker.message = response.successDataModel.tickerMessage
            presenter.tickerAnnouncementHolderData.value = ticker
        }
        view?.removeIneligiblePromo(notEligiblePromoHolderdata)
    }
}

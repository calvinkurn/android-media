package com.tokopedia.checkout.view.subscriber

import com.tokopedia.checkout.view.ShipmentContract
import com.tokopedia.checkout.view.ShipmentPresenter
import com.tokopedia.promocheckout.common.view.model.clearpromo.ClearPromoUiModel
import com.tokopedia.purchase_platform.common.feature.promonoteligible.NotEligiblePromoHolderdata
import rx.Subscriber
import java.util.*

/**
 * Created by Irfan Khoirul on 2019-06-25.
 */

class ClearNotEligiblePromoSubscriber(val view: ShipmentContract.View?,
                                      val presenter: ShipmentPresenter,
                                      val notEligiblePromoHolderdata: ArrayList<NotEligiblePromoHolderdata>) : Subscriber<ClearPromoUiModel>() {

    override fun onCompleted() {
    }

    override fun onError(e: Throwable) {
        e.printStackTrace()
        view?.removeIneligiblePromo(notEligiblePromoHolderdata)
    }

    override fun onNext(response: ClearPromoUiModel?) {
        if (response?.successDataModel?.tickerMessage?.isNotBlank() == true) {
            presenter.tickerAnnouncementHolderData.message = response.successDataModel.tickerMessage
            view?.updateTickerAnnouncementMessage()
        }
        view?.removeIneligiblePromo(notEligiblePromoHolderdata)
    }

}
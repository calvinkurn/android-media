package com.tokopedia.purchase_platform.features.checkout.view.subscriber

import android.text.TextUtils
import com.tokopedia.promocheckout.common.domain.model.clearpromo.ClearCacheAutoApplyStackResponse
import com.tokopedia.purchase_platform.features.checkout.view.ShipmentContract
import com.tokopedia.purchase_platform.features.checkout.view.ShipmentPresenter
import com.tokopedia.purchase_platform.features.checkout.view.uimodel.NotEligiblePromoHolderdata
import rx.Subscriber
import java.util.*

/**
 * Created by Irfan Khoirul on 2019-06-25.
 */

class ClearNotEligiblePromoSubscriber(val view: ShipmentContract.View?,
                                      val presenter: ShipmentPresenter,
                                      val checkoutType: Int,
                                      val notEligiblePromoHolderdata: ArrayList<NotEligiblePromoHolderdata>) : Subscriber<ClearCacheAutoApplyStackResponse>() {

    override fun onCompleted() {
    }

    override fun onError(e: Throwable) {
        e.printStackTrace()
        view?.hideLoading()
        view?.removeIneligiblePromo(checkoutType, notEligiblePromoHolderdata)
    }

    override fun onNext(response: ClearCacheAutoApplyStackResponse?) {
        view?.hideLoading()
        if (!TextUtils.isEmpty(response?.successData?.tickerMessage)) {
            presenter.tickerAnnouncementHolderData.message = response?.successData?.tickerMessage ?: ""
            view?.updateTickerAnnouncementMessage()
        }
        view?.removeIneligiblePromo(checkoutType, notEligiblePromoHolderdata)
    }

}
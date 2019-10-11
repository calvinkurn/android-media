package com.tokopedia.checkout.view.feature.shipment.subscriber

import android.text.TextUtils
import com.tokopedia.checkout.view.feature.shipment.ShipmentContract
import com.tokopedia.checkout.view.feature.shipment.ShipmentPresenter
import com.tokopedia.checkout.view.feature.shipment.viewmodel.NotEligiblePromoHolderdata
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.promocheckout.common.domain.model.clearpromo.ClearCacheAutoApplyStackResponse
import rx.Subscriber
import java.util.ArrayList

/**
 * Created by Irfan Khoirul on 2019-06-25.
 */

class ClearNotEligiblePromoSubscriber(val view: ShipmentContract.View?,
                                      val presenter: ShipmentPresenter,
                                      val checkoutType: Int,
                                      val notEligiblePromoHolderdata: ArrayList<NotEligiblePromoHolderdata>) : Subscriber<GraphqlResponse>() {

    override fun onCompleted() {
    }

    override fun onError(e: Throwable) {
        e.printStackTrace()
        view?.hideLoading()
        view?.removeIneligiblePromo(checkoutType, notEligiblePromoHolderdata)
    }

    override fun onNext(response: GraphqlResponse?) {
        view?.hideLoading()
        val responseData = response?.getData<ClearCacheAutoApplyStackResponse>(ClearCacheAutoApplyStackResponse::class.java)
        if (!TextUtils.isEmpty(responseData?.successData?.tickerMessage)) {
            presenter.tickerAnnouncementHolderData.message = responseData?.successData?.tickerMessage ?: ""
            view?.updateTickerAnnouncementMessage()
        }
        view?.removeIneligiblePromo(checkoutType, notEligiblePromoHolderdata)
    }

}
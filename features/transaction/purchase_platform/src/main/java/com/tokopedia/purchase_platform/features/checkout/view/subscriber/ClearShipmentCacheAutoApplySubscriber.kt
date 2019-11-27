package com.tokopedia.purchase_platform.features.checkout.view.subscriber

import android.text.TextUtils
import com.tokopedia.purchase_platform.features.checkout.view.ShipmentContract
import com.tokopedia.purchase_platform.features.checkout.view.ShipmentPresenter
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.promocheckout.common.domain.model.clearpromo.ClearCacheAutoApplyStackResponse
import rx.Subscriber

/**
 * Created by Irfan Khoirul on 21/03/19.
 */

class ClearShipmentCacheAutoApplySubscriber(val view: ShipmentContract.View?,
                                            val presenter: ShipmentContract.Presenter,
                                            val voucherType: String,
                                            val shopIndex: Int,
                                            val ignoreAPIResponse: Boolean) : Subscriber<GraphqlResponse>() {

    override fun onCompleted() {

    }

    override fun onError(e: Throwable) {
        e.printStackTrace()
        view?.hideLoading()
        if (!ignoreAPIResponse) {
            view?.onFailedClearPromoStack(ignoreAPIResponse)
        }
    }

    override fun onNext(response: GraphqlResponse) {
        view?.hideLoading()
        val responseData = response.getData<ClearCacheAutoApplyStackResponse>(ClearCacheAutoApplyStackResponse::class.java)
        if (ignoreAPIResponse) {
            if (responseData.successData.success) {
                onSuccess(responseData)
            }
        } else {
            if (responseData.successData.success) {
                onSuccess(responseData)
            } else {
                view?.onFailedClearPromoStack(ignoreAPIResponse)
            }
        }
    }

    private fun onSuccess(responseData: ClearCacheAutoApplyStackResponse) {
        if (!TextUtils.isEmpty(responseData.successData.tickerMessage)) {
            presenter.tickerAnnouncementHolderData.message = responseData.successData.tickerMessage
            view?.updateTickerAnnouncementMessage()
        }
        view?.onSuccessClearPromoStack(shopIndex, voucherType)
    }

}
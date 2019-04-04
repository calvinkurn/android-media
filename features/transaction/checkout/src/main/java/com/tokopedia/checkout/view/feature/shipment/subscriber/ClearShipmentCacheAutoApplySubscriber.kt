package com.tokopedia.checkout.view.feature.shipment.subscriber

import com.tokopedia.checkout.view.feature.shipment.ShipmentContract
import com.tokopedia.checkout.view.feature.shipment.ShipmentPresenter
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.promocheckout.common.domain.model.clearpromo.ClearCacheAutoApplyStackResponse
import rx.Subscriber

/**
 * Created by Irfan Khoirul on 21/03/19.
 */

class ClearShipmentCacheAutoApplySubscriber(val view: ShipmentContract.View?,
                                            val presenter: ShipmentPresenter,
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
                view?.onSuccessClearPromoStack(shopIndex)
            }
        } else {
            if (responseData.successData.success) {
                view?.onSuccessClearPromoStack(shopIndex)
            } else {
                view?.onFailedClearPromoStack(ignoreAPIResponse)
            }
        }
    }

}
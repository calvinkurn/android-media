package com.tokopedia.purchase_platform.features.cart.view.subscriber

import com.tokopedia.promocheckout.common.domain.model.clearpromo.ClearCacheAutoApplyStackResponse
import com.tokopedia.purchase_platform.features.cart.view.ICartListView
import rx.Subscriber

/**
 * Created by Irfan Khoirul on 21/03/19.
 */

class ClearCacheAutoApplySubscriber(val view: ICartListView?,
                                    val shopIndex: Int,
                                    val ignoreAPIResponse: Boolean) : Subscriber<ClearCacheAutoApplyStackResponse>() {

    override fun onCompleted() {

    }

    override fun onError(e: Throwable) {
        e.printStackTrace()
        view?.hideProgressLoading()
        if (!ignoreAPIResponse) {
            view?.onFailedClearPromoStack(ignoreAPIResponse)
        }
    }

    override fun onNext(response: ClearCacheAutoApplyStackResponse) {
        view?.hideProgressLoading()
        if (ignoreAPIResponse) {
            if (response.successData.success) {
                view?.onSuccessClearPromoStack(shopIndex)
            }
        } else {
            if (response.successData.success) {
                view?.onSuccessClearPromoStack(shopIndex)
            } else {
                view?.onFailedClearPromoStack(ignoreAPIResponse)
            }
        }
    }

}
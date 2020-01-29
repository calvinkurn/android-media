package com.tokopedia.purchase_platform.features.cart.view.subscriber

import com.tokopedia.purchase_platform.features.cart.data.model.response.recentview.GqlRecentViewResponse
import com.tokopedia.purchase_platform.features.cart.view.ICartListView
import rx.Subscriber

/**
 * Created by Irfan Khoirul on 21/09/18.
 */

class GetRecentViewSubscriber(private val view: ICartListView?) : Subscriber<GqlRecentViewResponse>() {

    override fun onCompleted() {

    }

    override fun onError(e: Throwable) {
        e.printStackTrace()
        view?.setHasTriedToLoadRecentView()
        view?.stopAllCartPerformanceTrace()
    }

    override fun onNext(response: GqlRecentViewResponse?) {
        view?.let {
            if (response?.gqlRecentView?.recentViewList?.size ?: 0 > 0) {
                view.renderRecentView(response?.gqlRecentView?.recentViewList)
            }
            view.setHasTriedToLoadRecentView()
            view.stopAllCartPerformanceTrace()
        }
    }

}

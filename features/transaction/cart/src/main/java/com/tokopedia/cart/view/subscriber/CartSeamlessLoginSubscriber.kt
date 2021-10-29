package com.tokopedia.cart.view.subscriber

import com.tokopedia.cart.view.ICartListView
import com.tokopedia.seamless_login_common.subscriber.SeamlessLoginSubscriber

class CartSeamlessLoginSubscriber(private val view: ICartListView?) : SeamlessLoginSubscriber {

    override fun onUrlGenerated(url: String) {
        view?.let { currentView ->
            currentView.hideProgressLoading()
            currentView.goToLite(url)
        }
    }

    override fun onError(msg: String) {
        view?.let { currentView ->
            currentView.hideProgressLoading()
            currentView.showToastMessageRed(msg)
        }
    }

}
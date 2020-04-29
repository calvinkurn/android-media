package com.tokopedia.shop.pageheader.presentation.subscriber
import com.tokopedia.shop.pageheader.presentation.listener.ShopPageView
import rx.Subscriber

class RequestShopModerateSubscriber(val view: ShopPageView):Subscriber<Boolean>() {

    override fun onNext(isSuccess: Boolean) {
        if (!isSuccess) {
            view.onErrorModerateListener(null)
            return
        }
        view.onSuccessModerateListener()
    }

    override fun onCompleted() {
    }

    override fun onError(e: Throwable?) {
        view.onErrorModerateListener(e)
    }
}
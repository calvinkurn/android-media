package com.tokopedia.shop.page.view.subscriber
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.shop.R
import com.tokopedia.shop.page.view.listener.ShopPageView
import rx.Subscriber

class RequestShopModerateSubscriber(val view: ShopPageView):Subscriber<Boolean>() {

    override fun onNext(isSuccess: Boolean) {
        if (!isSuccess) {
            val message = view.context.getString(R.string.moderate_shop_error)
            onError(MessageErrorException(message))
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
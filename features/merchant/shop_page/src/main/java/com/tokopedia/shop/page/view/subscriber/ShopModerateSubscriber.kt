package com.tokopedia.shop.page.view.subscriber
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.shop.R
import com.tokopedia.shop.page.view.listener.ShopPageView
import rx.Subscriber

class ShopModerateSubscriber(val view: ShopPageView):Subscriber<Boolean>() {

    override fun onNext(isSuccess: Boolean?) {
        if (isSuccess == false) {
            val message = view.context.getString(R.string.moderate_shop_error)
            onError(MessageErrorException(message))
            return
        }

        view.onSuccessModerateListener()
    }

    override fun onCompleted() {

    }

    override fun onError(e: Throwable?) {
        if (GlobalConfig.isAllowDebuggingTools()) {
            e?.printStackTrace()
        }
        view.onErrorModerateListener(ErrorHandler.getErrorMessage(view.context, e))
    }
}
package com.tokopedia.feedplus.view.subscriber

import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.feedplus.view.listener.FeedPlus
import rx.Subscriber


class FeedPlusDeletePostSubscriber(private val view: FeedPlus.View,
                                   private val id: Int,
                                   private val rowNumber: Int) : Subscriber<Boolean>() {
    override fun onNext(isSuccess: Boolean?) {
        if (isSuccess == null || isSuccess.not()) {
            onError(RuntimeException())
            return
        }
        view.onSuccessDeletePost(rowNumber)
    }

    override fun onCompleted() {
    }

    override fun onError(e: Throwable?) {
        if (GlobalConfig.isAllowDebuggingTools()) {
            e?.printStackTrace()
        }
        view.onErrorDeletePost(ErrorHandler.getErrorMessage(view.context, e), id, rowNumber)
    }
}

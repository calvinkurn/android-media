package com.tokopedia.profile.view.subscriber

import com.tokopedia.config.GlobalConfig
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.profile.view.listener.ProfileContract
import rx.Subscriber

/**
 * @author by milhamj on 10/15/18.
 */
class DeletePostSubscriber(private val view: ProfileContract.View,
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

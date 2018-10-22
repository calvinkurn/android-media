package com.tokopedia.profile.view.subscriber

import com.tokopedia.abstraction.common.utils.GlobalConfig
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.profile.view.listener.ProfileContract
import rx.Subscriber

/**
 * @author by milhamj on 10/17/18.
 */
class TrackPostClickSubscriber(private val view: ProfileContract.View,
                               private val uniqueTrackingId: String,
                               private val redirectLink: String) : Subscriber<Boolean>() {
    override fun onNext(isSuccess: Boolean?) {
        if (isSuccess!!.not()) {
            onError(RuntimeException())
        } else {
            view.onSuccessTrackPostClick(redirectLink)
        }
    }

    override fun onCompleted() {
    }

    override fun onError(e: Throwable?) {
        if (GlobalConfig.isAllowDebuggingTools()) {
            e?.printStackTrace()
        }
        view.hideLoadingLayout()
        view.onErrorTrackPostClick(
                ErrorHandler.getErrorMessage(view.context, e),
                uniqueTrackingId,
                redirectLink
        )
    }
}
package com.tokopedia.profile.view.subscriber

import com.tokopedia.profile.view.listener.ProfileContract
import rx.Subscriber

/**
 * @author by milhamj on 10/17/18.
 */
class TrackPostClickSubscriber(private val view: ProfileContract.View,
                               private val uniqueTrackingId: String,
                               private val redirectLink: String) : Subscriber<Boolean>() {
    override fun onNext(isSuccess: Boolean?) {
    }

    override fun onCompleted() {
    }

    override fun onError(e: Throwable?) {
    }
}
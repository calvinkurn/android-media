package com.tokopedia.feedcomponent.view.subscriber

import com.tokopedia.abstraction.common.utils.GlobalConfig
import rx.Subscriber

/**
 * Created by jegul on 2019-11-04
 */
class TrackPostClickSubscriber : Subscriber<Boolean>() {
    override fun onNext(isSuccess: Boolean?) {

    }

    override fun onCompleted() {
    }

    override fun onError(e: Throwable?) {
        if (GlobalConfig.isAllowDebuggingTools()) {
            e?.printStackTrace()
        }
    }
}
package com.tokopedia.power_merchant.subscribe.view.subscriber

import com.tokopedia.kotlin.extensions.view.debugTrace
import com.tokopedia.power_merchant.subscribe.view.contract.PmSubscribeContract
import rx.Subscriber

class GetInfoToggleAutoExtendSubscriber(private val view: PmSubscribeContract.View) : Subscriber<Boolean>() {

    override fun onNext(success: Boolean) {
        if (success) {
            view.onSuccessCancelMembership()
        } else {
            onError(RuntimeException())
        }
    }

    override fun onCompleted() {
    }

    override fun onError(e: Throwable?) {
        e?.debugTrace()
        e?.let { view.onErrorCancelMembership(it) }
    }
}
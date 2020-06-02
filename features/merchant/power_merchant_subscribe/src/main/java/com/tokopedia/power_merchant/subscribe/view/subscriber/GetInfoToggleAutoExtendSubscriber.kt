package com.tokopedia.power_merchant.subscribe.view.subscriber

import com.tokopedia.power_merchant.subscribe.view.contract.PmSubscribeContract
import rx.Subscriber
import timber.log.Timber

class GetInfoToggleAutoExtendSubscriber(private val view: PmSubscribeContract.View) : Subscriber<Boolean>() {

    override fun onNext(success: Boolean) {
        if (success) {
            view.hideLoading()
            view.onSuccessCancelMembership()
        } else {
            onError(RuntimeException())
        }
    }

    override fun onCompleted() {
    }

    override fun onError(e: Throwable?) {
        Timber.d(e)
        view.hideLoading()
        e?.let { view.onErrorCancelMembership(it) }
    }
}
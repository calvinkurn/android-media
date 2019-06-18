package com.tokopedia.power_merchant.subscribe.view.subscriber

import com.tokopedia.gm.common.data.source.cloud.model.PowerMerchantActivationResult
import com.tokopedia.kotlin.extensions.view.debugTrace
import com.tokopedia.power_merchant.subscribe.view.contract.PmSubscribeContract
import rx.Subscriber

class GetInfoToggleAutoExtendSubscriber(private val view: PmSubscribeContract.View): Subscriber<PowerMerchantActivationResult>() {

    override fun onNext(t: PowerMerchantActivationResult) {
        view.onSuccessCancelMembership()
    }

    override fun onCompleted() {
    }

    override fun onError(e: Throwable?) {
        e?.debugTrace()
        e?.let { view.onErrorCancelMembership(it) }
    }
}
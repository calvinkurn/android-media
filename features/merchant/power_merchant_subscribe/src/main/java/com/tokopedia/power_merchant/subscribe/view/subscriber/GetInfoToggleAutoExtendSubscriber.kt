package com.tokopedia.power_merchant.subscribe.view.subscriber

import com.tokopedia.gm.common.data.source.cloud.model.PowerMerchantActivationResult
import com.tokopedia.power_merchant.subscribe.view.contract.PmSubscribeContract
import rx.Subscriber

class GetInfoToggleAutoExtendSubscriber(private val view: PmSubscribeContract.View): Subscriber<PowerMerchantActivationResult>() {

    override fun onNext(t: PowerMerchantActivationResult) {
        view.refreshData()
    }

    override fun onCompleted() {
    }

    override fun onError(e: Throwable?) {
        e?.let { view.showEmptyState(it) }
    }
}
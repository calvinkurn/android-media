package com.tokopedia.power_merchant.subscribe.view.subscriber

import com.tokopedia.gm.common.data.source.cloud.model.PowerMerchantStatus
import com.tokopedia.power_merchant.subscribe.view.contract.PmSubscribeContract
import rx.Subscriber

class GetPmStatusInfoSubscriber(private val view: PmSubscribeContract.View) : Subscriber<PowerMerchantStatus>() {
    override fun onNext(data: PowerMerchantStatus) {
        view.onSuccessGetPmInfo(data)
    }

    override fun onCompleted() {

    }

    override fun onError(e: Throwable) {
        view.onErrorGetPmInfo(e)
    }
}
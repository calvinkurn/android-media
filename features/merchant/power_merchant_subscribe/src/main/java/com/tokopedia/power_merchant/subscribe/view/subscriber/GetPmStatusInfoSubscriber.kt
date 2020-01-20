package com.tokopedia.power_merchant.subscribe.view.subscriber

import com.tokopedia.gm.common.data.source.cloud.model.PowerMerchantStatus
import com.tokopedia.power_merchant.subscribe.view.contract.PmSubscribeContract
import rx.Subscriber
import timber.log.Timber

class GetPmStatusInfoSubscriber(private val view: PmSubscribeContract.View) : Subscriber<PowerMerchantStatus>() {
    override fun onNext(data: PowerMerchantStatus) {
        view.onSuccessGetPmInfo(data)
    }

    override fun onCompleted() {

    }

    override fun onError(e: Throwable) {
        Timber.d(e)
        view.showEmptyState(e)
    }
}
package com.tokopedia.power_merchant.subscribe.subscriber

import com.tokopedia.gm.common.data.source.cloud.model.PowerMerchantStatus
import com.tokopedia.gm.common.data.source.cloud.model.ShopStatusModel
import com.tokopedia.power_merchant.subscribe.contract.PmSubscribeContract
import rx.Subscriber

class GetPmStatusInfoSubscriber(private val view: PmSubscribeContract.View) : Subscriber<PowerMerchantStatus>() {
    override fun onNext(data: PowerMerchantStatus) {
        view.onSuccessGetPmInfo(data)
    }

    override fun onCompleted() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onError(e: Throwable) {
        view.onErrorGetPmInfo(e)
    }
}
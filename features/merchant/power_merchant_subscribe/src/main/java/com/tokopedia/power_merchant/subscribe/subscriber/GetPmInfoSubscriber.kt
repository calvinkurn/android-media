package com.tokopedia.power_merchant.subscribe.subscriber

import com.tokopedia.gm.common.data.source.cloud.model.ShopStatusModel
import com.tokopedia.power_merchant.subscribe.contract.PmSubscribeContract
import rx.Subscriber

class GetPmInfoSubscriber(private val view: PmSubscribeContract.View) : Subscriber<ShopStatusModel>() {


    override fun onNext(data: ShopStatusModel) {
//        view.onSuccessGetPmInfo(data)
    }

    override fun onCompleted() {
    }

    override fun onError(e: Throwable) {
        view.onErrorGetPmInfo(e)
    }
}
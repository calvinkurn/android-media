package com.tokopedia.shop.oldpage.view.subscriber

import com.tokopedia.shop.common.data.source.cloud.model.ShopModerateRequestData
import com.tokopedia.shop.oldpage.view.listener.ShopPageView
import rx.Subscriber

class GetShopModerateSubscriber(val view: ShopPageView) : Subscriber<ShopModerateRequestData>() {

    override fun onNext(shopModerateRequestData: ShopModerateRequestData?) {
        view.onSuccessGetModerateInfo(shopModerateRequestData)
    }

    override fun onCompleted() {
    }

    override fun onError(e: Throwable?) {
        view.onErrorModerateListener(e)
    }


}
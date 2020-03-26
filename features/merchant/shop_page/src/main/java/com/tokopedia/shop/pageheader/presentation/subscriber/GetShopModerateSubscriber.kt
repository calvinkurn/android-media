package com.tokopedia.shop.pageheader.presentation.subscriber

import com.tokopedia.shop.common.data.source.cloud.model.ShopModerateRequestData
import com.tokopedia.shop.pageheader.presentation.listener.ShopPageView
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
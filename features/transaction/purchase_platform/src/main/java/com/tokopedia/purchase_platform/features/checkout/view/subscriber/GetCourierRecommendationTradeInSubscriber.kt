package com.tokopedia.purchase_platform.features.checkout.view.subscriber

import com.tokopedia.logisticcart.shipping.model.ShippingRecommendationData
import rx.Subscriber

/**
 * Created by Irfan Khoirul on 2019-11-04.
 */

class GetCourierRecommendationTradeInSubscriber(val shipperId: Int,
                                                val shipperProductId: Int) : Subscriber<ShippingRecommendationData>() {

    override fun onCompleted() {

    }

    override fun onError(e: Throwable?) {

    }

    override fun onNext(t: ShippingRecommendationData) {

    }

}
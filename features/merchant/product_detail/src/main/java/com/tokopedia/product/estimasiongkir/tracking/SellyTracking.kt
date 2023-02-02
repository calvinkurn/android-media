package com.tokopedia.product.estimasiongkir.tracking

import com.tokopedia.trackingoptimizer.TrackingQueue

object SellyTracking {

    fun impressScheduledDelivery(
        trackingQueue: TrackingQueue,
        data: SellyTracker.ImpressionComponent
    ) {
        val items = ArrayList(
            data.prices.mapIndexed { index, price ->
                hashMapOf(
                    "creative_name" to price.first,
                    "creative_slot" to index.toString(),
                    "item_id" to "pengiriman terjadwal",
                    "item_name" to price.second
                )
            }
        )

        val mapEvent = hashMapOf<String, Any>(
            "event" to "promoView",
            "eventAction" to "impression - scheduled delivery detail bottomsheet",
            "eventCategory" to "product detail page",
            "eventLabel" to "product_id:${data.productId};shop_district_id:${data.sellerDistrictId};buyer_district_id:${data.buyerDistrictId};berat_satuan:${data.beratSatuan};",
            "trackerId" to "40899",
            "businessUnit" to "product detail page",
            "component" to "bottomsheet scheduled delivery",
            "currentSite" to "tokopediamarketplace",
            "layout" to data.layoutId,
            "productId" to data.productId,
            "ecommerce" to hashMapOf(
                "promoView" to hashMapOf(
                    "promotions" to items
                )
            ),
            "shopId" to data.shopId,
            "userId" to data.userId
        )

        trackingQueue.putEETracking(mapEvent)
    }
}

package com.tokopedia.similarsearch

import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

internal fun eventUserSeeSimilarProduct(productId: String, productsItem: List<Any>) {
    TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            DataLayer.mapOf(
                    TrackAppUtils.EVENT, Event.PRODUCT_VIEW,
                    TrackAppUtils.EVENT_CATEGORY, Category.SIMILAR_PRODUCT,
                    TrackAppUtils.EVENT_ACTION, Action.IMPRESSION_PRODUCT,
                    TrackAppUtils.EVENT_LABEL, String.format(Label.PRODUCT_ID, productId),
                    ECOMMERCE, DataLayer.mapOf(
                            "currencyCode", "IDR",
                            "impressions", DataLayer.listOf(productsItem.toTypedArray())
                    )
            )
    )
}
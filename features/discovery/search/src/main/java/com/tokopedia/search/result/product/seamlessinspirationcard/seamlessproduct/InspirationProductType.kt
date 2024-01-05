package com.tokopedia.search.result.product.seamlessinspirationcard.seamlessproduct

import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselDataView
import com.tokopedia.search.result.product.seamlessinspirationcard.utils.TRACKER_PRODUCT_SEAMLESS_INSPIRATION_PRODUCT

class SeamlessInspirationItemProduct(
    val type: String,
    val inspirationCarouselProduct: InspirationCarouselDataView.Option.Product,
    val hasThreeDots: Boolean = true
) {
    fun getDataLayerList(): String {
        return TRACKER_PRODUCT_SEAMLESS_INSPIRATION_PRODUCT
    }
}

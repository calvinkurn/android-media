package com.tokopedia.search.result.product.semlessproduct.seamlessproduct

import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselDataView.Option.Product

interface InspirationProductItemTracker {

    fun trackInspirationProductSeamlessImpression(
        type: String,
        product: Product,
    )

    fun trackInspirationProductSeamlessClick(
        type: String,
        product: Product,
    )
}

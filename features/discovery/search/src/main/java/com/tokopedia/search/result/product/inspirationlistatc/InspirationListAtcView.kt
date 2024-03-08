package com.tokopedia.search.result.product.inspirationlistatc

import com.tokopedia.product.detail.common.data.model.aggregator.ProductVariantResult
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselDataView
import com.tokopedia.search.result.product.inspirationcarousel.analytics.InspirationCarouselTracking

interface InspirationListAtcView {

    fun trackSeeMoreClick(data: InspirationCarouselDataView.Option)

    fun trackItemClick(trackingData: InspirationCarouselTracking.Data)

    fun trackItemImpress(product: InspirationCarouselDataView.Option.Product)

    fun trackAddToCart(trackingData: InspirationCarouselTracking.Data)

    fun openAddToCartToaster(message: String, isSuccess: Boolean)

    fun openVariantBottomSheet(
        product: InspirationCarouselDataView.Option.Product,
        type: String,
        onCheckout: () -> Unit
    )

    fun trackAddToCartVariant(product: InspirationCarouselDataView.Option.Product)

    fun updateSearchBarNotification()

    fun trackAdsClick(product: InspirationCarouselDataView.Option.Product)

    fun trackAdsImpress(product: InspirationCarouselDataView.Option.Product)
}

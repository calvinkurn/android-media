package com.tokopedia.search.result.product.inspirationlistatc

import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselDataView
import com.tokopedia.search.result.product.inspirationcarousel.analytics.InspirationCarouselTrackingUnification

interface InspirationListAtcView {

    fun trackSeeMoreClick(data: InspirationCarouselDataView.Option)

    fun trackItemClick(trackingData: InspirationCarouselTrackingUnification.Data)

    fun trackItemImpress(product: InspirationCarouselDataView.Option.Product)

    fun trackAddToCart(trackingData: InspirationCarouselTrackingUnification.Data)

    fun openAddToCartToaster(addToCartDataModel: AddToCartDataModel?)

    fun openVariantBottomSheet(
        product: InspirationCarouselDataView.Option.Product,
        type: String,
    )

    fun trackAddToCartVariant(product: InspirationCarouselDataView.Option.Product)

    fun updateSearchBarNotification()
}

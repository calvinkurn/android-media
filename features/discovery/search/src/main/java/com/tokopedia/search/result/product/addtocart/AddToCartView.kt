package com.tokopedia.search.result.product.addtocart

import com.tokopedia.search.result.presentation.model.ProductItemDataView
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselDataView
import com.tokopedia.search.result.product.inspirationcarousel.analytics.InspirationCarouselTrackingUnification

interface AddToCartView {

    fun openAddToCartToaster(message: String, isSuccess: Boolean)
    fun openVariantBottomSheet(
        data: ProductItemDataView,
        type: String,
    )
    fun updateSearchBarNotification()
}

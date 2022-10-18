package com.tokopedia.search.result.product.addtocart.analytics

import com.tokopedia.search.result.presentation.model.ProductItemDataView

interface AddToCartTracking {
    fun trackItemClick(productItemDataView: ProductItemDataView)
}

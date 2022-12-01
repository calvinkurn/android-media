package com.tokopedia.search.result.product.inspirationlistatc

import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselDataView

interface InspirationListAtcPresenter {
    fun onListAtcItemAddToCart(
        product: InspirationCarouselDataView.Option.Product,
        type: String,
    )
}

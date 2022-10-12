package com.tokopedia.search.result.product.inspirationlistatc

import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselDataView

interface InspirationListAtcPresenter {
    val productAddedToCart: InspirationCarouselDataView.Option.Product
    fun onListAtcItemAddToCart(
        product: InspirationCarouselDataView.Option.Product,
        type: String,
    )
}

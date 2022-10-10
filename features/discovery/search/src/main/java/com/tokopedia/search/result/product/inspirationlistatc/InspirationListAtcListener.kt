package com.tokopedia.search.result.product.inspirationlistatc

import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselDataView

interface InspirationListAtcListener {
    fun onListAtcSeeMoreClicked(data: InspirationCarouselDataView.Option)
    fun onListAtcItemClicked(product: InspirationCarouselDataView.Option.Product)
    fun onListAtcItemImpressed(product: InspirationCarouselDataView.Option.Product)
    fun onListAtcItemAddToCart(product: InspirationCarouselDataView.Option.Product, type: String)
}

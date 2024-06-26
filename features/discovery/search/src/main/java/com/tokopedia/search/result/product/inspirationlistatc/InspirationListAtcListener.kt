package com.tokopedia.search.result.product.inspirationlistatc

import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselDataView
import com.tokopedia.search.result.product.inspirationlistatc.postatccarousel.InspirationListPostAtcDataView

interface InspirationListAtcListener {
    fun onListAtcSeeMoreClicked(data: InspirationCarouselDataView.Option)
    fun onListAtcItemClicked(product: InspirationCarouselDataView.Option.Product)
    fun onListAtcItemImpressed(product: InspirationCarouselDataView.Option.Product)
    fun onListAtcItemAddToCart(product: InspirationCarouselDataView.Option.Product, type: String)
    fun onListAtcImpressed(data: InspirationCarouselDataView.Option)
}

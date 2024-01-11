package com.tokopedia.search.result.product.inspirationlistatc.postatccarousel

import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselDataView

interface InspirationListPostAtcListener {
    fun onListAtcSeeMoreClicked(data: InspirationCarouselDataView.Option)
    fun onListAtcItemClicked(product: InspirationCarouselDataView.Option.Product)
    fun onListAtcItemImpressed(product: InspirationCarouselDataView.Option.Product)
    fun onListAtcItemAddToCart(product: InspirationCarouselDataView.Option.Product, type: String)
    fun closeListPostAtcView(item: InspirationListPostAtcDataView)
    fun cancelCloseListPostAtcView(item: InspirationListPostAtcDataView)
}

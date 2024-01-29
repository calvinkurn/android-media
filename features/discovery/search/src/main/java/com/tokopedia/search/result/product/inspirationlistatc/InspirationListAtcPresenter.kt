package com.tokopedia.search.result.product.inspirationlistatc

import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselDataView
import com.tokopedia.search.result.product.inspirationlistatc.postatccarousel.InspirationListPostAtcDataView

interface InspirationListAtcPresenter {
    fun onListAtcItemAddToCart(
        product: InspirationCarouselDataView.Option.Product,
        type: String,
    )

    fun setVisibilityInspirationCarouselPostAtcOnVisitableList(
        visibility: Boolean,
        item: InspirationListPostAtcDataView,
    )
}

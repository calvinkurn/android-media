package com.tokopedia.search.result.product.videowidget

import com.tokopedia.search.result.presentation.model.InspirationCarouselDataView

interface InspirationVideoCarouselListener {
    fun onInspirationVideoCarouselProductImpressed(product: InspirationCarouselDataView.Option.Product)

    fun onInspirationVideoCarouselProductClicked(product: InspirationCarouselDataView.Option.Product)
}
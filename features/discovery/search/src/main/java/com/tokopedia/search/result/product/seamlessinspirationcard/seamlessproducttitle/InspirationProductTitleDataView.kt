package com.tokopedia.search.result.product.seamlessinspirationcard.seamlessproducttitle

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory
import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselDataView

data class InspirationProductTitleDataView(
    val inspirationCarouselDataView: InspirationCarouselDataView,
): Visitable<ProductListTypeFactory> {

    override fun type(typeFactory: ProductListTypeFactory?): Int =
        typeFactory?.type(this) ?: 0

    companion object {

        fun create(inspirationCarouselDataView: InspirationCarouselDataView) =
            InspirationProductTitleDataView(inspirationCarouselDataView)
    }
}

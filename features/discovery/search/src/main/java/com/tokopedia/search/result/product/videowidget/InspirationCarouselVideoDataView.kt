package com.tokopedia.search.result.product.videowidget

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.search.result.presentation.model.InspirationCarouselDataView
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory

data class InspirationCarouselVideoDataView(
    val data: InspirationCarouselDataView
) : Visitable<ProductListTypeFactory> {
    override fun type(typeFactory: ProductListTypeFactory): Int {
        return typeFactory.type(this)
    }
}
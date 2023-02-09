package com.tokopedia.productcard_compact.productcardcarousel.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.productcard_compact.productcardcarousel.presentation.adapter.typefactory.ProductCardCompactCarouselTypeFactory

data class ProductCardCompactCarouselSeeMoreUiModel (
    val id: String = "",
    val headerName: String = "",
    val appLink: String = ""
): Visitable<ProductCardCompactCarouselTypeFactory>{
    override fun type(typeFactory: ProductCardCompactCarouselTypeFactory): Int {
        return typeFactory.type(this)
    }
}

package com.tokopedia.productcard_compact.productcardcarousel.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.productcard_compact.productcardcarousel.presentation.adapter.typefactory.TokoNowProductCardCarouselTypeFactory

data class TokoNowSeeMoreCardCarouselUiModel (
    val id: String = "",
    val headerName: String = "",
    val appLink: String = ""
): Visitable<TokoNowProductCardCarouselTypeFactory>{
    override fun type(typeFactory: TokoNowProductCardCarouselTypeFactory): Int {
        return typeFactory.type(this)
    }
}

package com.tokopedia.tokopedianow.common.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowProductCardCarouselTypeFactory

data class TokoNowSeeMoreCardCarouselUiModel (
    val id: String = "",
    val headerName: String = "",
    val appLink: String = ""
): Visitable<TokoNowProductCardCarouselTypeFactory>{
    override fun type(typeFactory: TokoNowProductCardCarouselTypeFactory): Int {
        return typeFactory.type(this)
    }
}

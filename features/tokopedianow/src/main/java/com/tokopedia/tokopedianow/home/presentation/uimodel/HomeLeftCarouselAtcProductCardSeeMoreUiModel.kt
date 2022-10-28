package com.tokopedia.tokopedianow.home.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.home.presentation.adapter.HomeLeftCarouselAtcProductCardTypeFactory

data class HomeLeftCarouselAtcProductCardSeeMoreUiModel (
    val channelId: String = "",
    val channelHeaderName: String = "",
    val appLink: String = "",
    val backgroundImage: String = ""
): Visitable<HomeLeftCarouselAtcProductCardTypeFactory>{
    override fun type(typeFactory: HomeLeftCarouselAtcProductCardTypeFactory): Int {
        return typeFactory.type(this)
    }
}
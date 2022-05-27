package com.tokopedia.tokopedianow.home.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.home.presentation.adapter.HomeLeftCarouselProductCardTypeFactory

data class HomeLeftCarouselProductCardSpaceUiModel(
    val appLink: String = "",
    val channelId: String = "",
    val channelHeaderName: String = ""
): Visitable<HomeLeftCarouselProductCardTypeFactory> {
    override fun type(typeFactory: HomeLeftCarouselProductCardTypeFactory): Int {
        return typeFactory.type(this)
    }
}
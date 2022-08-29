package com.tokopedia.tokopedianow.home.presentation.adapter

import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLeftCarouselAtcProductCardSeeMoreUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLeftCarouselAtcProductCardUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLeftCarouselAtcProductCardSpaceUiModel

interface HomeLeftCarouselAtcProductCardTypeFactory {
    fun type(uiModel: HomeLeftCarouselAtcProductCardUiModel): Int
    fun type(uiModel: HomeLeftCarouselAtcProductCardSpaceUiModel): Int
    fun type(uiModel: HomeLeftCarouselAtcProductCardSeeMoreUiModel): Int
}
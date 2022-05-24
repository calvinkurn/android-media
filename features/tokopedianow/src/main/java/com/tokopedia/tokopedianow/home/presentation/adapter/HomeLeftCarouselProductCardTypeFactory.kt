package com.tokopedia.tokopedianow.home.presentation.adapter

import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLeftCarouselProductCardSeeMoreUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLeftCarouselProductCardUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLeftCarouselProductCardSpaceUiModel

interface HomeLeftCarouselProductCardTypeFactory {
    fun type(uiModel: HomeLeftCarouselProductCardUiModel): Int
    fun type(uiModel: HomeLeftCarouselProductCardSpaceUiModel): Int
    fun type(uiModel: HomeLeftCarouselProductCardSeeMoreUiModel): Int
}
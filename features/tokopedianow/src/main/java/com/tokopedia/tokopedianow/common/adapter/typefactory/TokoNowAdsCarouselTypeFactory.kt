package com.tokopedia.tokopedianow.common.adapter.typefactory

import com.tokopedia.tokopedianow.common.model.TokoNowAdsCarouselUiModel

interface TokoNowAdsCarouselTypeFactory {
    fun type(uiModel: TokoNowAdsCarouselUiModel): Int
}

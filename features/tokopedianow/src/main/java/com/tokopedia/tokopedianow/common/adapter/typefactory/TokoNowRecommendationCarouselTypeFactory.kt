package com.tokopedia.tokopedianow.common.adapter.typefactory

import com.tokopedia.tokopedianow.common.model.TokoNowRecommendationCarouselUiModel

interface TokoNowRecommendationCarouselTypeFactory {
    fun type(uiModel: TokoNowRecommendationCarouselUiModel): Int
}
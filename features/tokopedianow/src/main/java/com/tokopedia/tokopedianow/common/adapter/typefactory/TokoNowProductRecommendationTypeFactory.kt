package com.tokopedia.tokopedianow.common.adapter.typefactory

import com.tokopedia.tokopedianow.common.model.TokoNowProductRecommendationUiModel

interface TokoNowProductRecommendationTypeFactory {
    fun type(uiModel: TokoNowProductRecommendationUiModel): Int
}

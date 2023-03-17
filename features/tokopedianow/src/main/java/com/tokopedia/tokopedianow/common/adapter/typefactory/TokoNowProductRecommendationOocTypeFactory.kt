package com.tokopedia.tokopedianow.common.adapter.typefactory

import com.tokopedia.tokopedianow.common.model.TokoNowProductRecommendationOocUiModel

interface TokoNowProductRecommendationOocTypeFactory {
    fun type(uiModel: TokoNowProductRecommendationOocUiModel): Int
}


package com.tokopedia.tokopedianow.common.model

data class TokoNowProductRecommendationViewUiModel(
    val headerModel: TokoNowDynamicHeaderUiModel,
    val seeMoreModel: TokoNowSeeMoreCardCarouselUiModel,
    val productModels: List<TokoNowProductCardCarouselItemUiModel>
)

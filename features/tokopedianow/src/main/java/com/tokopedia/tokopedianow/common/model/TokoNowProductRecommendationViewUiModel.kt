package com.tokopedia.tokopedianow.common.model

import com.tokopedia.productcard_compact.productcardcarousel.presentation.uimodel.TokoNowProductCardCarouselItemUiModel
import com.tokopedia.productcard_compact.productcardcarousel.presentation.uimodel.TokoNowSeeMoreCardCarouselUiModel
import com.tokopedia.productcard_compact.productcardcarousel.presentation.uimodel.TokoNowDynamicHeaderUiModel

data class TokoNowProductRecommendationViewUiModel(
    val headerModel: TokoNowDynamicHeaderUiModel,
    val seeMoreModel: TokoNowSeeMoreCardCarouselUiModel,
    val productModels: List<TokoNowProductCardCarouselItemUiModel>
)

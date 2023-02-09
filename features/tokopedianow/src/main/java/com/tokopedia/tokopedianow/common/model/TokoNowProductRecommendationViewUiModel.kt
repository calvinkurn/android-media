package com.tokopedia.tokopedianow.common.model

import com.tokopedia.productcard_compact.productcardcarousel.presentation.uimodel.ProductCardCompactCarouselItemUiModel
import com.tokopedia.productcard_compact.productcardcarousel.presentation.uimodel.ProductCardCompactCarouselSeeMoreUiModel

data class TokoNowProductRecommendationViewUiModel(
    val headerModel: TokoNowDynamicHeaderUiModel,
    val seeMoreModel: ProductCardCompactCarouselSeeMoreUiModel,
    val productModels: List<ProductCardCompactCarouselItemUiModel>
)

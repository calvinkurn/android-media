package com.tokopedia.tokopedianow.common.analytics.model

import com.tokopedia.productcard.compact.productcardcarousel.presentation.uimodel.ProductCardCompactCarouselItemUiModel

data class AddToCartDataTrackerModel(
    val position: Int,
    val quantity: Int,
    val cartId: String,
    val productRecommendation: ProductCardCompactCarouselItemUiModel
)

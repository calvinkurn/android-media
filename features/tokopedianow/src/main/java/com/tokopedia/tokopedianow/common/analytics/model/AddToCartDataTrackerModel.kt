package com.tokopedia.tokopedianow.common.analytics.model

import com.tokopedia.productcard_compact.productcardcarousel.presentation.uimodel.TokoNowProductCardCarouselItemUiModel

data class AddToCartDataTrackerModel(
    val position: Int,
    val quantity: Int,
    val cartId: String,
    val productRecommendation: TokoNowProductCardCarouselItemUiModel
)

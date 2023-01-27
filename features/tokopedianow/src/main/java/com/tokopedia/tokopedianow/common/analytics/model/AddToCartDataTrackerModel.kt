package com.tokopedia.tokopedianow.common.analytics.model

import com.tokopedia.tokopedianow.common.model.TokoNowProductCardCarouselItemUiModel

data class AddToCartDataTrackerModel(
    val position: Int,
    val quantity: Int,
    val cartId: String,
    val productRecommendation: TokoNowProductCardCarouselItemUiModel
)

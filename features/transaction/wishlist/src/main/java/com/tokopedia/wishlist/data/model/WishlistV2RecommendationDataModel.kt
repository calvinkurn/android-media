package com.tokopedia.wishlist.data.model

import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget

data class WishlistV2RecommendationDataModel(
    val recommendationData: List<RecommendationWidget>,
    val isCarousel: Boolean = false
) : WishlistV2Data

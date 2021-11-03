package com.tokopedia.wishlist.data.model

import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

data class WishlistV2RecommendationWrapper(
    val recommendationData: RecommendationItem
) : WishlistV2Data

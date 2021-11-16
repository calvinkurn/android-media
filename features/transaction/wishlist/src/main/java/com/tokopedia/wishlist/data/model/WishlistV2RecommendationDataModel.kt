package com.tokopedia.wishlist.data.model

import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

data class WishlistV2RecommendationDataModel(
    val recommendationData: List<RecommendationItem>,
    val title: String = ""
) : WishlistV2Data

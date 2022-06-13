package com.tokopedia.wishlist.data.model

import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

data class WishlistV2RecommendationDataModel(
        val recommendationProductCardModelData: List<ProductCardModel> = listOf(),
        val listRecommendationItem: List<RecommendationItem> = listOf(),
        val title: String = "")

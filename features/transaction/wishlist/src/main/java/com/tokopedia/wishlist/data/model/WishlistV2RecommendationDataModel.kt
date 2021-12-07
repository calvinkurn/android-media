package com.tokopedia.wishlist.data.model

import com.tokopedia.productcard.ProductCardModel

data class WishlistV2RecommendationDataModel(
        val recommendationData: List<ProductCardModel> = listOf(),
        val listRecommendationId: List<Long> = listOf(),
        val title: String = ""
)

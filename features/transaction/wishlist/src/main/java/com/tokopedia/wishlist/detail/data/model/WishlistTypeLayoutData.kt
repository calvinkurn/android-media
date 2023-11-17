package com.tokopedia.wishlist.detail.data.model

import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

data class WishlistTypeLayoutData(
        val dataObject: Any? = Any(),
        var typeLayout: String? = "",
        val wishlistItem: WishlistUiModel.Item = WishlistUiModel.Item(),
        var isChecked: Boolean = false,
        val recommItem: RecommendationItem = RecommendationItem()
)

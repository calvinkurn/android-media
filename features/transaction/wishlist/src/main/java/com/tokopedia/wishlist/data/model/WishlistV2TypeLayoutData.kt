package com.tokopedia.wishlist.data.model

import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

data class WishlistV2TypeLayoutData(
        val dataObject: Any? = Any(),
        var typeLayout: String? = "",
        val wishlistItem: WishlistV2UiModel.Item = WishlistV2UiModel.Item(),
        var isChecked: Boolean = false,
        val recommItem: RecommendationItem = RecommendationItem())
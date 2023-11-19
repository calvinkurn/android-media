package com.tokopedia.wishlist.collection.data.model

import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

data class WishlistCollectionTypeLayoutData(
    val dataObject: Any? = Any(),
    var typeLayout: String? = "",
    val recommItem: RecommendationItem = RecommendationItem()
)

package com.tokopedia.home_wishlist.model.action

data class RemoveWishlistRecommendationData(
        override val isSuccess: Boolean = false,
        override val message: String = ""
): BaseActionData()
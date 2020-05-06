package com.tokopedia.home_wishlist.model.action

data class AddWishlistRecommendationData(
        override val message: String = "",
        override val isSuccess: Boolean = false
) : BaseActionData()
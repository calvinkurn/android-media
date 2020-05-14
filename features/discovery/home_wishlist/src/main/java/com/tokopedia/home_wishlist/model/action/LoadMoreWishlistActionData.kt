package com.tokopedia.home_wishlist.model.action

data class LoadMoreWishlistActionData(
        override val isSuccess: Boolean = false,
        val hasNextPage: Boolean = false,
        override val message: String = ""
): BaseActionData()
package com.tokopedia.discovery.common.model

data class WishlistTrackingModel(
        var isAddWishlist: Boolean = false,
        var productId: String = "",
        var isTopAds: Boolean = false,
        var keyword: String = "",
        var isUserLoggedIn: Boolean = false
)
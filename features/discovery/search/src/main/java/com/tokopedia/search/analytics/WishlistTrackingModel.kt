package com.tokopedia.search.analytics

class WishlistTrackingModel(
        var isAddWishlist: Boolean = false,
        var productId: String = "",
        var isTopAds: Boolean = false,
        var keyword: String = "",
        var isUserLoggedIn: Boolean = false,
        var userId: String = "0"
)
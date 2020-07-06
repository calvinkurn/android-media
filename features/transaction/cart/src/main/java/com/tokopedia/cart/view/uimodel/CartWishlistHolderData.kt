package com.tokopedia.cart.view.uimodel

/**
 * Created by Irfan Khoirul on 2019-06-15.
 */

data class CartWishlistHolderData(
        var hasSentImpressionAnalytics: Boolean = false,
        var lastFocussPosition: Int = 0,
        var wishList: List<CartWishlistItemHolderData> = arrayListOf()
)
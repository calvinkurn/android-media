package com.tokopedia.cart.view.uimodel

/**
 * Created by Irfan Khoirul on 2019-06-15.
 */

data class CartRecentViewItemHolderData(
        var id: String = "",
        var name: String = "",
        var price: String = "",
        var imageUrl: String = "",
        var isWishlist: Boolean = false,
        var rating: Int = 0,
        var reviewCount: Int = 0,
        var badgeUrl: String = "",
        var shopLocation: String = "",
        var shopId: String = "",
        var shopName: String = "",
        var shopType: String = "",
        var minOrder: Int = 1
)
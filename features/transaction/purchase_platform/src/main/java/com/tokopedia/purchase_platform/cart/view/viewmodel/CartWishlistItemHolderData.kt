package com.tokopedia.purchase_platform.cart.view.viewmodel

/**
 * Created by Irfan Khoirul on 2019-05-31.
 */

data class CartWishlistItemHolderData(
        var id: String = "",
        var name: String = "",
        var rawPrice: String = "",
        var price: String = "",
        var imageUrl: String = "",
        var isWishlist: Boolean = false,
        var rating: Int = 0,
        var reviewCount: Int = 0,
        var badgeUrl: String = "",
        var shopLocation: String = "",
        var shopId: String = "",
        var shopType: String = "",
        var shopName: String = "",
        var minOrder: Int = 1,
        var category: String = "",
        var url: String = ""
)
package com.tokopedia.checkout.view.feature.cartlist.viewmodel

/**
 * Created by Irfan Khoirul on 2019-05-31.
 */

data class CartWishlistItemHolderData(
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
        var minOrder: Int = 1
)
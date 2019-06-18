package com.tokopedia.checkout.view.feature.cartlist.viewmodel

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
        var shopLocation: String = ""
)
package com.tokopedia.checkout.view.feature.cartlist.viewmodel

/**
 * Created by Irfan Khoirul on 2019-05-31.
 */

data class CartWishlistItemHolderData(
        var id: String = "",
        var name: String = "",
        var price: String = "",
        var imageUrl: String = "",
        var isWishlist: Boolean = false
)
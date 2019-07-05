package com.tokopedia.checkout.view.feature.cartlist.viewmodel

/**
 * Created by Irfan Khoirul on 2019-06-15.
 */

data class CartWishlistHolderData(
        var lastFocussPosition: Int = 0,
        var wishList: List<CartWishlistItemHolderData> = arrayListOf()
)
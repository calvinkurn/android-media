package com.tokopedia.cart.view.uimodel

/**
 * Created by Irfan Khoirul on 2019-06-15.
 */

data class CartWishlistHolderData(
        var lastFocussPosition: Int = 0,
        var wishList: MutableList<CartWishlistItemHolderData> = arrayListOf(),
        var hasInitializeRecyclerView: Boolean = false
)
package com.tokopedia.wishlist.data.model

data class WishlistV2DataModel(val item: WishlistV2Response.Data.WishlistV2.Item, var isChecked: Boolean = false) : WishlistV2Data

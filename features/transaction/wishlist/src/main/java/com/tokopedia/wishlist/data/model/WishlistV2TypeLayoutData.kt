package com.tokopedia.wishlist.data.model

import com.tokopedia.wishlist.data.model.response.WishlistV2Response

data class WishlistV2TypeLayoutData(
        val dataObject: Any = Any(),
        val typeLayout: String? = "",
        val wishlistItem: WishlistV2Response.Data.WishlistV2.Item = WishlistV2Response.Data.WishlistV2.Item(),
        var isChecked: Boolean = false)
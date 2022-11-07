package com.tokopedia.tokopedianow.common.domain.model

import com.google.gson.annotations.SerializedName

data class RemoveFromWishListResponse(

    @SerializedName("wishlist_remove_v2")
    val wishlistRemove: WishlistData? = null
)

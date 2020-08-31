package com.tokopedia.wishlist.common.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Irfan Khoirul on 29/01/20.
 */

data class GetWishlistResponse(
        @SerializedName("wishlist")
        @Expose
        var gqlWishList: WishlistDataResponse? = null
)

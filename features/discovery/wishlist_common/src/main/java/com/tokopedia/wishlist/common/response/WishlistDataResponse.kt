package com.tokopedia.wishlist.common.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.wishlist.common.data.source.cloud.model.Pagination
import com.tokopedia.wishlist.common.data.source.cloud.model.Wishlist
import java.util.*

/**
 * Created by Irfan Khoirul on 29/01/20.
 */

data class WishlistDataResponse(
        @SerializedName("has_next_page")
        @Expose
        var isHasNextPage: Boolean = false,

        @SerializedName("items")
        @Expose
        var wishlistDataList: List<Wishlist> = ArrayList(),

        @SerializedName("pagination")
        @Expose
        var pagination: Pagination? = null
)
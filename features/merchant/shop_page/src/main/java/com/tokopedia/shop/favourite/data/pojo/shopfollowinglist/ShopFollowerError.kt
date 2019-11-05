package com.tokopedia.shop.favourite.data.pojo.shopfollowinglist

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShopFollowerError(
        @SerializedName("message")
        @Expose
        val message: String
)
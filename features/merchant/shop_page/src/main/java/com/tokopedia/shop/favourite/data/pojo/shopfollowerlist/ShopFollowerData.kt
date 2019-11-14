package com.tokopedia.shop.favourite.data.pojo.shopfollowerlist

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShopFollowerData(
        @SerializedName("followerID")
        @Expose
        val followerID: String,

        @SerializedName("followerName")
        @Expose
        val followerName: String,

        @SerializedName("photo")
        @Expose
        val photo: String,

        @SerializedName("profileURL")
        @Expose
        val profileURL: String
)
package com.tokopedia.shop.favourite.data.pojo.shopfollowerlist

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShopFollowerList(
        @SerializedName("data")
        @Expose
        val shopFollowerData: List<ShopFollowerData>,

        @SerializedName("error")
        @Expose
        val error: Error,

        @SerializedName("haveNext")
        @Expose
        val haveNext: Boolean
)
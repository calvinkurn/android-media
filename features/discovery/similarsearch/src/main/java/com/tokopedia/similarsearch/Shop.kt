package com.tokopedia.similarsearch

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

internal data class Shop(
        @SerializedName("id")
        @Expose
        val id: Int = 0,

        @SerializedName("is_gold_shop")
        @Expose
        val isGoldShop: Boolean = false,

        @SerializedName("location")
        @Expose
        val location: String = "",

        @SerializedName("city")
        @Expose
        val city: String = "",

        @SerializedName("reputation")
        @Expose
        val reputation: String = "",

        @SerializedName("clover")
        @Expose
        val clover: String = "",

        @SerializedName("is_official")
        @Expose
        val isOfficial: Boolean = false
)
package com.tokopedia.shop.common.graphql.data.shopinfo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShopCore(
        @SerializedName("description")
        @Expose
        val description: String = "",

        @SerializedName("domain")
        @Expose
        val domain: String = "",

        @SerializedName("name")
        @Expose
        val name: String = "",

        @SerializedName("shopID")
        @Expose
        val shopID: String = "",

        @SerializedName("tagLine")
        @Expose
        val tagLine: String = "",

        @SerializedName("url")
        @Expose
        val url: String = ""
)
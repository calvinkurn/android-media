package com.tokopedia.product.detail.data.model.shop

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShopCore(
        @SerializedName("domain")
        @Expose
        val domain: String = "",

        @SerializedName("name")
        @Expose
        val name: String = "",

        @SerializedName("shopID")
        @Expose
        val shopID: String = "",

        @SerializedName("url")
        @Expose
        val url: String = ""
)
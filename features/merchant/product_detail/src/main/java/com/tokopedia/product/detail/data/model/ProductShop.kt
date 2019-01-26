package com.tokopedia.product.detail.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductShop(
        @SerializedName("domain")
        @Expose
        val domain: String = "",

        @SerializedName("id")
        @Expose
        val id: Int = 0,

        @SerializedName("name")
        @Expose
        val name: String = "",

        @SerializedName("url")
        @Expose
        val url: String = ""
)
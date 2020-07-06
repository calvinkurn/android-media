package com.tokopedia.discovery2.data.cpmtopads

import com.google.gson.annotations.SerializedName

data class Shop(

        @SerializedName("product")
        val product: ArrayList<ProductItem?>? = null,

        @SerializedName("name")
        val name: String? = "",

        @SerializedName("id")
        val id: String? = "",

        @SerializedName("gold_shop_badge")
        val goldShopBadge: Boolean? = false,

        @SerializedName("slogan")
        val slogan: String? = ""
)
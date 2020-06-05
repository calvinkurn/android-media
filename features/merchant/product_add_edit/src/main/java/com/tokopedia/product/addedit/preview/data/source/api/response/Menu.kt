package com.tokopedia.product.addedit.preview.data.source.api.response

import com.google.gson.annotations.SerializedName

data class Menu(
        @SerializedName("menuID")
        val menuID: String = "",
        @SerializedName("name")
        val name: String = "",
        @SerializedName("url")
        val url: String = "",
        @SerializedName("alias")
        val alias: String = "",
        @SerializedName("productCount")
        val productCount: Int = 0
)
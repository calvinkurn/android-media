package com.tokopedia.product.addedit.preview.data.source.api.response

import com.google.gson.annotations.SerializedName

data class Brand(
        @SerializedName("brandID")
        val brandID: String = "",
        @SerializedName("name")
        val name: String = "",
        @SerializedName("brandStatus")
        val brandStatus: Int = 0,
        @SerializedName("isActive")
        val isActive: Boolean = false
)
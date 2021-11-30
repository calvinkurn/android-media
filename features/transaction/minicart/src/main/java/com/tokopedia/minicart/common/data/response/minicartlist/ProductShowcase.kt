package com.tokopedia.minicart.common.data.response.minicartlist

import com.google.gson.annotations.SerializedName

data class ProductShowcase(
        @SerializedName("id")
        val id: String = "",
        @SerializedName("name")
        val name: String = ""
)
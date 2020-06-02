package com.tokopedia.product.addedit.preview.data.source.api.response

import com.google.gson.annotations.SerializedName

data class Catalog(
        @SerializedName("catalogID")
        val catalogID: String = "",
        @SerializedName("name")
        val name: String = "",
        @SerializedName("url")
        val url: String = ""
)
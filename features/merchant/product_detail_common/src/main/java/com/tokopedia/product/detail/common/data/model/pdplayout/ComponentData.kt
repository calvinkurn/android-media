package com.tokopedia.product.detail.common.data.model.pdplayout


import com.google.gson.annotations.SerializedName

data class ComponentData(
        @SerializedName("Applink")
        val applink: String = "",
        @SerializedName("Content")
        val content: List<Content> = listOf(),
        @SerializedName("Row")
        val row: String = ""
)
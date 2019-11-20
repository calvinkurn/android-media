package com.tokopedia.product.detail.common.data.model.pdplayout


import com.google.gson.annotations.SerializedName

data class ComponentData(
        @SerializedName("applink")
        val applink: String = "",
        @SerializedName("content")
        val content: List<Content> = listOf(),
        @SerializedName("row")
        val row: String = "",
        @SerializedName("title")
        val title: String = ""

)
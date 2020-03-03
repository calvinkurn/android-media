package com.tokopedia.product.detail.common.data.model.pdplayout


import com.google.gson.annotations.SerializedName

data class Component(
        @SerializedName("data")
        val componentData: List<ComponentData> = listOf(),
        @SerializedName("name")
        val componentName: String = "",
        @SerializedName("type")
        val type: String = ""
)
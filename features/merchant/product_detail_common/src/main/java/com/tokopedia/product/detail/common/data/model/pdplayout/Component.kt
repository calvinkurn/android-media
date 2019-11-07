package com.tokopedia.product.detail.common.data.model.pdplayout


import com.google.gson.annotations.SerializedName

data class Component(
        @SerializedName("Data")
        val componentData: List<ComponentData> = listOf(),
        @SerializedName("Name")
        val componentName: String = "",
        @SerializedName("Type")
        val type: String = ""
)
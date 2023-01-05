package com.tokopedia.product.detail.common.data.model.pdplayout


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Component(
    @SerializedName("data")
    @Expose
    val componentData: List<ComponentData> = listOf(),
    @SerializedName("name")
    @Expose
    val componentName: String = "",
    @SerializedName("type")
    @Expose
    val type: String = ""
)

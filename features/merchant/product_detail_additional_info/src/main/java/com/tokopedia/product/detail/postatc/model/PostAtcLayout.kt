package com.tokopedia.product.detail.postatc.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PostAtcLayout(
    @SerializedName("components")
    @Expose
    val components: List<Component>
) {
    data class Component(
        @SerializedName("name")
        @Expose
        val name: String,

        @SerializedName("type")
        @Expose
        val type: String,

        @SerializedName("data")
        @Expose
        val data: ComponentData
    )

    sealed class ComponentData
}

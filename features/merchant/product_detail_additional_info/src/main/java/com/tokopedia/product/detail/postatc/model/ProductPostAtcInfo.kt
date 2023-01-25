package com.tokopedia.product.detail.postatc.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductPostAtcInfo(
    @SerializedName("title")
    @Expose
    val title: String,

    @SerializedName("subTitle")
    @Expose
    val subtitle: String,

    @SerializedName("image")
    @Expose
    val image: String,

    @SerializedName("button")
    @Expose
    val button: Button
) : PostAtcLayout.ComponentData() {
    data class Button(
        @SerializedName("text")
        @Expose
        val text: String
    )
}

package com.tokopedia.product.detail.postatc.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PostAtcComponentData(
    @SerializedName("title")
    @Expose
    val title: String = "",

    @SerializedName("subTitle")
    @Expose
    val subtitle: String = "",

    @SerializedName("image")
    @Expose
    val image: String = "",

    @SerializedName("button")
    @Expose
    val button: Button = Button()
) {
    data class Button(
        @SerializedName("text")
        @Expose
        val text: String = "",

        @SerializedName("cartID")
        @Expose
        val cartId: String = ""
    )
}

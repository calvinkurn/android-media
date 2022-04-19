package com.tokopedia.cart.data.model.response.shopgroupsimplified

import com.google.gson.annotations.SerializedName

data class EmptyCart(
        @SerializedName("image")
        val image: String = "",
        @SerializedName("title")
        val title: String = "",
        @SerializedName("description")
        val description: String = "",
        @SerializedName("buttons")
        val buttons: List<Button> = emptyList()
)
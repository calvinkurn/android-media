package com.tokopedia.minicart.common.data.response.minicartlist

import com.google.gson.annotations.SerializedName

data class EmptyCart(
        @SerializedName("buttons")
        val buttons: List<Button> = emptyList(),
        @SerializedName("description")
        val description: String = "",
        @SerializedName("image")
        val image: String = "",
        @SerializedName("title")
        val title: String = ""
)
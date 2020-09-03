package com.tokopedia.cart.data.model.response.updatecart

import com.google.gson.annotations.SerializedName
import com.tokopedia.cart.data.model.response.shopgroupsimplified.Button

data class PromptPage(
        @SerializedName("image")
        val image: String = "",
        @SerializedName("title")
        val title: String = "",
        @SerializedName("descriptions")
        val descriptions: String = "",
        @SerializedName("buttons")
        val buttons: List<Button> = emptyList()
)
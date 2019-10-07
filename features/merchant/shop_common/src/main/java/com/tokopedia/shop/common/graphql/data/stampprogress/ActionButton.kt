package com.tokopedia.shop.common.graphql.data.stampprogress


import com.google.gson.annotations.SerializedName

data class ActionButton(
    @SerializedName("isShown")
    val isShown: Boolean = false,

    @SerializedName("text")
    val text: String = ""
)
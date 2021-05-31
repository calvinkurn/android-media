package com.tokopedia.minicart.common.data.response.minicartlist

import com.google.gson.annotations.SerializedName

data class OutOfService(
        @SerializedName("buttons")
        val buttons: List<Button> = emptyList(),
        @SerializedName("code")
        val code: String = "",
        @SerializedName("description")
        val description: String = "",
        @SerializedName("id")
        val id: String = "",
        @SerializedName("image")
        val image: String = "",
        @SerializedName("title")
        val title: String = ""
)
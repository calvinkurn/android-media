package com.tokopedia.checkout.data.model.response.checkout

import com.google.gson.annotations.SerializedName

data class PromptResponse(
        @SerializedName("type")
        val type: String = "",
        @SerializedName("title")
        val title: String = "",
        @SerializedName("description")
        val description: String = "",
        @SerializedName("buttons")
        val buttons: List<PromptButtonResponse> = emptyList()
)

data class PromptButtonResponse(
        @SerializedName("text")
        val text: String = "",
        @SerializedName("link")
        val link: String = "",
        @SerializedName("color")
        val color: String = "",
        @SerializedName("action")
        val action: String = ""
)
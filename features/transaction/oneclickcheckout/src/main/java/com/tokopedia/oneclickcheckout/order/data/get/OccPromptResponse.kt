package com.tokopedia.oneclickcheckout.order.data.get

import com.google.gson.annotations.SerializedName

data class OccPromptResponse(
        @SerializedName("type")
        val type: String = "",
        @SerializedName("title")
        val title: String = "",
        @SerializedName("description")
        val description: String = "",
        @SerializedName("image_url")
        val imageUrl: String = "",
        @SerializedName("buttons")
        val buttons: List<OccPromptButtonResponse> = emptyList()
)

data class OccPromptButtonResponse(
        @SerializedName("text")
        val text: String = "",
        @SerializedName("link")
        val link: String = "",
        @SerializedName("action")
        val action: String = "",
        @SerializedName("color")
        val color: String = ""
)
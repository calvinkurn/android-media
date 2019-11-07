package com.tokopedia.product.detail.common.data.model.pdplayout


import com.google.gson.annotations.SerializedName

data class Content(
        @SerializedName("Icon")
        val icon: String = "",
        @SerializedName("Text")
        val text: String = "",
        @SerializedName("Subtitle")
        val subtitle: String = "",
        @SerializedName("Type")
        val componentType: String = "",
        @SerializedName("Rating")
        val componentRating: String = ""
)
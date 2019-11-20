package com.tokopedia.product.detail.common.data.model.pdplayout


import com.google.gson.annotations.SerializedName

data class Content(
        @SerializedName("icon")
        val icon: String = "",
        @SerializedName("text")
        val text: String = "",
        @SerializedName("subtitle")
        val subtitle: String = "",
        @SerializedName("type")
        val componentType: String = "",
        @SerializedName("rating")
        val componentRating: String = ""
)
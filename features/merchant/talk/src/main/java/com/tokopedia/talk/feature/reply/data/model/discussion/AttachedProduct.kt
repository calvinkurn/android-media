package com.tokopedia.talk.feature.reply.data.model.discussion

import com.google.gson.annotations.SerializedName

data class AttachedProduct(
        @SerializedName("productID")
        val productId: String = "",
        @SerializedName("thumbnail")
        val thumbnail: String = "",
        @SerializedName("name")
        val name: String = "",
        @SerializedName("url")
        val url: String = "",
        @SerializedName("priceFormatted")
        val priceFormatted: String = ""
)
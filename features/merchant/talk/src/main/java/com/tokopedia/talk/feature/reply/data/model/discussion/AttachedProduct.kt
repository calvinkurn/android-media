package com.tokopedia.talk.feature.reply.data.model.discussion

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AttachedProduct(
        @SerializedName("productID")
        @Expose
        val productId: String = "",
        @SerializedName("thumbnail")
        @Expose
        val thumbnail: String = "",
        @SerializedName("name")
        @Expose
        val name: String = "",
        @SerializedName("url")
        @Expose
        val url: String = "",
        @SerializedName("priceFormatted")
        @Expose
        val priceFormatted: String = ""
)
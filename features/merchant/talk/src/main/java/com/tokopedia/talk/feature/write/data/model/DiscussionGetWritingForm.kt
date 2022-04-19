package com.tokopedia.talk.feature.write.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DiscussionGetWritingForm(
        @SerializedName("productName")
        @Expose
        val productName: String = "",
        @SerializedName("productThumbnailURL")
        @Expose
        val productThumbnailUrl: String = "",
        @SerializedName("productID")
        @Expose
        val productId: String = "",
        @SerializedName("productPrice")
        @Expose
        val productPrice: String = "",
        @SerializedName("shopID")
        @Expose
        val shopId: String = "",
        @SerializedName("minChar")
        @Expose
        val minChar: Int = 0,
        @SerializedName("maxChar")
        @Expose
        val maxChar: Int = 0,
        @SerializedName("categories")
        @Expose
        val categories: List<DiscussionGetWritingFormCategory> = emptyList()
)
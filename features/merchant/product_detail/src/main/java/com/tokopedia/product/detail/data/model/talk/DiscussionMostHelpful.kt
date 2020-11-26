package com.tokopedia.product.detail.data.model.talk

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DiscussionMostHelpful(
        @SerializedName("totalQuestion")
        @Expose
        val totalQuestion: Int = 0,
        @SerializedName("question")
        @Expose
        val questions: List<Question>? = null,
        @SerializedName("productID")
        @Expose
        val productId: String = "",
        @SerializedName("shopID")
        @Expose
        val shopId: String = "",
        @SerializedName("shopURL")
        @Expose
        val shopUrl: String = ""
)
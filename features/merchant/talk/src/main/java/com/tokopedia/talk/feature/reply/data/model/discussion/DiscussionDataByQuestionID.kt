package com.tokopedia.talk.feature.reply.data.model.discussion

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DiscussionDataByQuestionID(
        @SerializedName("shopID")
        @Expose
        val shopID: String = "",
        @SerializedName("shopURL")
        @Expose
        val shopURL: String = "",
        @SerializedName("question")
        @Expose
        val question: Question = Question(),
        @SerializedName("maxAnswerLength")
        @Expose
        val maxAnswerLength: Int = 0,
        @SerializedName("productName")
        @Expose
        val productName: String = "",
        @SerializedName("thumbnail")
        @Expose
        val thumbnail: String = "",
        @SerializedName("url")
        @Expose
        val url: String = "",
        @SerializedName("productID")
        @Expose
        val productId: String = "",
        @SerializedName("productStock")
        @Expose
        val productStock: Int = 0,
        @SerializedName("productStockMessage")
        @Expose
        val productStockMessage: String = "",
        @SerializedName("isSellerView")
        @Expose
        val isSellerView: Boolean = false
)
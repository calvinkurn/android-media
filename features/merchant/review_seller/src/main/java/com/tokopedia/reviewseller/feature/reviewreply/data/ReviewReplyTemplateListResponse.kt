package com.tokopedia.reviewseller.feature.reviewreply.data

import com.google.gson.annotations.SerializedName

data class ReviewReplyTemplateListResponse(
        @SerializedName("reviewResponseTemplateList")
        val reviewResponseTemplateList: ReviewResponseTemplateList = ReviewResponseTemplateList()
) {
    data class ReviewResponseTemplateList(
            @SerializedName("autoReply")
            val autoReply: AutoReply = AutoReply(),
            @SerializedName("list")
            val list: List<ReplyTemplate> = listOf()
    ) {
        data class AutoReply(
                @SerializedName("autoReplyId")
                val autoReplyId: Int? = 0,
                @SerializedName("status")
                val status: Int? = 0,
                @SerializedName("templateId")
                val templateId: Int? = 0
        )
        data class ReplyTemplate(
                @SerializedName("message")
                val message: String? = "",
                @SerializedName("status")
                val status: Int? = 0,
                @SerializedName("templateId")
                val templateId: Int? = 0,
                @SerializedName("title")
                val title: String? = ""
        )
    }
}
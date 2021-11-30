package com.tokopedia.review.feature.reviewreply.data

import com.google.gson.annotations.SerializedName

data class ReviewReplyTemplateListResponse(
        @SerializedName("reviewResponseTemplateListV2")
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
                val autoReplyId: String? = "0",
                @SerializedName("status")
                val status: Long? = 0,
                @SerializedName("templateId")
                val templateId: String? = "0"
        )
        data class ReplyTemplate(
                @SerializedName("message")
                val message: String? = "",
                @SerializedName("status")
                val status: String? = "0",
                @SerializedName("templateId")
                val templateId: String? = "0",
                @SerializedName("title")
                val title: String? = ""
        )
    }
}
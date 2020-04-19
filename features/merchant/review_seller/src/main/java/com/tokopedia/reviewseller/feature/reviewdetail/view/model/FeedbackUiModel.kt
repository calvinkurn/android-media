package com.tokopedia.reviewseller.feature.reviewdetail.view.model

data class FeedbackUiModel(
        var attachments: List<Attachment> = listOf(),
        var autoReply: String? = "",
        var feedbackID: Int? = -1,
        var rating: Int? = -1,
        var replyText: String? = "",
        var replyTime: String? = "",
        var reviewText: String? = "",
        var reviewTime: String? = "",
        var reviewerName: String? = "",
        var variantName: String? = ""
) {
    data class Attachment(
            val thumbnailURL: String? = "",
            val fullSizeURL: String? = ""
    )
}
package com.tokopedia.review.feature.inboxreview.presentation.model

import com.tokopedia.review.feature.inboxreview.presentation.adapter.BaseInboxReview
import com.tokopedia.review.feature.inboxreview.presentation.adapter.InboxReviewAdapterTypeFactory

data class FeedbackInboxUiModel(
        var feedbackId: Int = 0,
        var rating: Int = 0,
        var userID: Int = 0,
        var username: String = "",
        var productID: Int = 0,
        var productName: String = "",
        var productImageUrl: String = "",
        var productVariant: String = "",
        var attachments: List<Attachment> = mutableListOf(),
        var variantID: Int = 0,
        var variantName: String = "",
        var invoiceID: String = "",
        var reviewText: String = "",
        var reviewTime: String = "",
        var replyText: String = "",
        var replyTime: String = "",
        var isAutoReply: Boolean = false,
        var sellerUser: String = "",
        var isMoreReply: Boolean = false,
        var isReplied: Boolean = false,
        var isKejarUlasan: Boolean = false
): BaseInboxReview {

    data class Attachment(
            var thumbnailURL: String = "",
            var fullSizeURL: String = ""
    )

    override fun type(typeFactory: InboxReviewAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}
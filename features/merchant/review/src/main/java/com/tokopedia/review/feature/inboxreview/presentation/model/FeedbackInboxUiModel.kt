package com.tokopedia.review.feature.inboxreview.presentation.model

import com.tokopedia.review.feature.inboxreview.presentation.adapter.BaseInboxReview
import com.tokopedia.review.feature.inboxreview.presentation.adapter.InboxReviewAdapterTypeFactory
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uimodel.ReviewMediaThumbnailUiModel

data class FeedbackInboxUiModel(
    var feedbackId: String = "",
    var rating: Int = 0,
    var userID: String = "",
    var username: String = "",
    var productID: String = "",
    var productName: String = "",
    var productImageUrl: String = "",
    var productVariant: String = "",
    var imageAttachments: List<ImageAttachment> = mutableListOf(),
    var videoAttachments: List<VideoAttachment> = mutableListOf(),
    var reviewMediaThumbnail: ReviewMediaThumbnailUiModel = ReviewMediaThumbnailUiModel(listOf()),
    var variantID: String = "",
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
    var isKejarUlasan: Boolean = false,
    var ratingDisclaimer: String = "",
    var badRatingReason: String = ""
) : BaseInboxReview {

    data class ImageAttachment(
        var thumbnailURL: String = "",
        var fullSizeURL: String = ""
    )

    data class VideoAttachment(
        var videoUrl: String = ""
    )

    override fun type(typeFactory: InboxReviewAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}
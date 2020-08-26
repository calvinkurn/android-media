package com.tokopedia.review.feature.reviewreply.view.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FeedbackReplyUiModel(
        var attachments: List<Attachment> = mutableListOf(),
        var autoReply: String? = "",
        var feedbackID: Int? = 0,
        var rating: Int? = 0,
        var replyText: String? = "",
        var replyTime: String? = "",
        var reviewText: String? = "",
        var isMoreReply: Boolean = false,
        var reviewTime: String? = "",
        var reviewerName: String? = "",
        var variantName: String? = "",
        var sellerUser: String? = "",
        var page : Int? = 0
): Parcelable {

    @Parcelize
    data class Attachment(
            var thumbnailURL: String? = "",
            var fullSizeURL: String? = ""
    ): Parcelable
}
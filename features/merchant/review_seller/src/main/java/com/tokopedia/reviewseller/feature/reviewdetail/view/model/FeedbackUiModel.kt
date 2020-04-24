package com.tokopedia.reviewseller.feature.reviewdetail.view.model

import com.tokopedia.reviewseller.feature.reviewdetail.view.adapter.BaseSellerReviewDetail
import com.tokopedia.reviewseller.feature.reviewdetail.view.adapter.SellerReviewDetailAdapterTypeFactory

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
        var variantName: String? = "",
        var sellerUser: String? = ""
): BaseSellerReviewDetail {

    override fun type(typeFactory: SellerReviewDetailAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

    data class Attachment(
            val thumbnailURL: String? = "",
            val fullSizeURL: String? = ""
    )
}
package com.tokopedia.review.feature.reviewdetail.view.model

import com.tokopedia.review.feature.reviewdetail.view.adapter.BaseSellerReviewDetail
import com.tokopedia.review.feature.reviewdetail.view.adapter.SellerReviewDetailAdapterTypeFactory

data class FeedbackUiModel(
        var attachments: List<Attachment> = mutableListOf(),
        var autoReply: Boolean = false,
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
        var isKejarUlasan: Boolean = false,
        var page : Int? = 0
): BaseSellerReviewDetail {

    override fun type(typeFactory: SellerReviewDetailAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

    data class Attachment(
            var thumbnailURL: String? = "",
            var fullSizeURL: String? = ""
    )
}
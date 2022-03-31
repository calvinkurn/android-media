package com.tokopedia.review.feature.reviewdetail.view.model

import android.os.Parcelable
import com.tokopedia.review.feature.reviewdetail.view.adapter.BaseSellerReviewDetail
import com.tokopedia.review.feature.reviewdetail.view.adapter.SellerReviewDetailAdapterTypeFactory
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uimodel.ReviewMediaThumbnailUiModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class FeedbackUiModel(
    var imageAttachments: List<ImageAttachment> = mutableListOf(),
    var videoAttachments: List<VideoAttachment> = mutableListOf(),
    var reviewMediaThumbnail: ReviewMediaThumbnailUiModel = ReviewMediaThumbnailUiModel(listOf()),
    var autoReply: Boolean = false,
    var feedbackID: String = "",
    var productID: String = "",
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
    var page: Int? = 0,
    var badRatingReason: String = "",
    var badRatingDisclaimer: String = ""
) : BaseSellerReviewDetail, Parcelable {

    override fun type(typeFactory: SellerReviewDetailAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

    @Parcelize
    data class ImageAttachment(
        var thumbnailURL: String? = "",
        var fullSizeURL: String? = ""
    ) : Parcelable

    @Parcelize
    data class VideoAttachment(
        var videoUrl: String? = ""
    ) : Parcelable
}
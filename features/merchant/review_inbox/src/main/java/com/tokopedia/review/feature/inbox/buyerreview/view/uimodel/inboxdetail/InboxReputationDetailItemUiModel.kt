package com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.review.feature.inbox.buyerreview.view.adapter.typefactory.inboxdetail.InboxReputationDetailTypeFactory
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.model.ProductrevGetReviewMedia
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uimodel.ReviewMediaThumbnailUiModel

/**
 * @author by nisie on 8/19/17.
 */
class InboxReputationDetailItemUiModel(
    val reputationId: String,
    var productId: String,
    var productName: String,
    var productAvatar: String,
    var productUrl: String,
    val reviewId: String,
    var reviewerName: String,
    var reviewTime: String,
    var reviewMediaThumbnailUiModel: ReviewMediaThumbnailUiModel,
    var preloadedDetailedReviewMedia: ProductrevGetReviewMedia,
    var review: String,
    var reviewStar: Int,
    val isReviewHasReviewed: Boolean,
    val isReviewIsEditable: Boolean,
    var isReviewSkipped: Boolean,
    val shopId: String,
    var tab: Int,
    val reviewResponseUiModel: ReviewResponseUiModel,
    var isReviewIsAnonymous: Boolean,
    var isProductDeleted: Boolean,
    val isReviewIsEdited: Boolean,
    val reviewerId: String,
    val isProductBanned: Boolean,
    private val productStatus: Int,
    var orderId: String
) : Visitable<InboxReputationDetailTypeFactory> {

    companion object {
        const val IS_LIKED: Int = 1
    }

    override fun type(typeFactory: InboxReputationDetailTypeFactory): Int {
        return typeFactory.type(this)
    }

    fun getproductStatus(): Int {
        return productStatus
    }
}

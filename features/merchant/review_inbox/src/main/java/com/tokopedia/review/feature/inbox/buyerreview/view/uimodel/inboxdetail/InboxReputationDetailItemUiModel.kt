package com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.review.feature.inbox.buyerreview.view.adapter.typefactory.inboxdetail.InboxReputationDetailTypeFactory

/**
 * @author by nisie on 8/19/17.
 */
class InboxReputationDetailItemUiModel (
    val reputationId: Long,
    var productId: String,
    var productName: String,
    var productAvatar: String,
    var productUrl: String,
    val reviewId: String,
    var reviewerName: String,
    var reviewTime: String,
    var reviewAttachment: List<ImageAttachmentUiModel>,
    var review: String,
    var reviewStar: Int,
    val isReviewHasReviewed: Boolean,
    val isReviewIsEditable: Boolean,
    val isReviewIsSkippable: Boolean,
    var isReviewSkipped: Boolean,
    val shopId: Long,
    var tab: Int,
    val reviewResponseUiModel: ReviewResponseUiModel,
    var isReviewIsAnonymous: Boolean,
    var isProductDeleted: Boolean,
    val isReviewIsEdited: Boolean,
    var revieweeName: String,
    val reviewerId: Long,
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
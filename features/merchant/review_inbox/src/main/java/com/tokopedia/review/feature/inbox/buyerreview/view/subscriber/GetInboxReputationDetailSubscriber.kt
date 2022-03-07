package com.tokopedia.review.feature.inbox.buyerreview.view.subscriber

import android.text.TextUtils
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.InboxReputationDomain
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.InboxReputationItemDomain
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.ReputationBadgeDomain
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.ReputationDataDomain
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.RevieweeBadgeCustomerDomain
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.RevieweeBadgeSellerDomain
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.ImageAttachmentDomain
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.InboxReputationDetailDomain
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.ReviewDomain
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.ReviewItemDomain
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.ReviewResponseDomain
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.ShopDataDomain
import com.tokopedia.review.feature.inbox.buyerreview.view.listener.InboxReputationDetail
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.InboxReputationItemUiModel
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.InboxReputationUiModel
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.ReputationDataUiModel
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail.ImageAttachmentUiModel
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail.InboxReputationDetailItemUiModel
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail.ReputationBadgeUiModel
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail.ReviewResponseUiModel
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail.RevieweeBadgeCustomerUiModel
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail.RevieweeBadgeSellerUiModel
import rx.Subscriber

/**
 * @author by nisie on 8/19/17.
 */
open class GetInboxReputationDetailSubscriber constructor(
    protected val viewListener: InboxReputationDetail.View
) : Subscriber<InboxReputationDetailDomain>() {

    companion object {
        const val PRODUCT_IS_DELETED: Int = 0
        const val PRODUCT_IS_BANNED: Int = -2
    }

    override fun onCompleted() {}

    override fun onError(e: Throwable) {
        viewListener.finishLoading()
        viewListener.onErrorGetInboxDetail(e)
    }

    override fun onNext(inboxReputationDetailDomain: InboxReputationDetailDomain) {
        viewListener.finishLoading()
        viewListener.onSuccessGetInboxDetail(
            convertToReputationViewModel(inboxReputationDetailDomain.inboxReputationDomain).list.getOrNull(
                0
            ) ?: InboxReputationItemUiModel(),
            mappingToListItemViewModel(inboxReputationDetailDomain.reviewDomain)
        )
    }

    private fun convertToReputationBadgeViewModel(reputationBadge: ReputationBadgeDomain): ReputationBadgeUiModel {
        return ReputationBadgeUiModel(
            reputationBadge.level,
            reputationBadge.set
        )
    }

    protected fun mappingToListItemViewModel(reviewDomain: ReviewDomain): List<Visitable<*>> {
        return reviewDomain.data.map {
            convertToInboxReputationDetailItemViewModel(
                reviewDomain,
                it
            )
        }
    }

    private fun convertToInboxReputationDetailItemViewModel(
        reviewDomain: ReviewDomain, itemDomain: ReviewItemDomain
    ): Visitable<*> {
        return InboxReputationDetailItemUiModel(
            reviewDomain.reputationId,
            itemDomain.productData.productId.toString(),
            itemDomain.productData.productName,
            itemDomain.productData.productImageUrl,
            itemDomain.productData.productImageUrl,
            itemDomain.reviewData.reviewId.toString(),
            reviewDomain.userData.fullName,
            if (TextUtils.isEmpty(itemDomain.reviewData.reviewUpdateTime.dateTimeFmt1)) itemDomain.reviewData
                .reviewCreateTime.dateTimeFmt1 else itemDomain.reviewData.reviewUpdateTime.dateTimeFmt1,
            convertToImageAttachmentViewModel(itemDomain.reviewData.reviewImageUrl),
            itemDomain.reviewData.reviewMessage,
            itemDomain.reviewData.reviewRating,
            itemDomain.isReviewHasReviewed,
            itemDomain.isReviewIsEditable,
            itemDomain.isReviewIsSkippable,
            itemDomain.isReviewIsSkipped,
            reviewDomain.shopData.shopId,
            viewListener.tab,
            convertToReviewResponseViewModel(
                reviewDomain.shopData,
                itemDomain.reviewData.reviewResponse
            ),
            itemDomain.reviewData.isReviewAnonymity,
            itemDomain.productData.productStatus == PRODUCT_IS_DELETED,
            !TextUtils.isEmpty(
                itemDomain.reviewData.reviewUpdateTime.dateTimeFmt1
            ),
            reviewDomain.shopData.shopName,
            reviewDomain.userData.userId,
            itemDomain.productData.productStatus == PRODUCT_IS_BANNED,
            itemDomain.productData.productStatus,
            reviewDomain.orderId
        )
    }

    private fun convertToReviewResponseViewModel(
        shopData: ShopDataDomain,
        reviewResponse: ReviewResponseDomain
    ): ReviewResponseUiModel {
        return ReviewResponseUiModel(
            reviewResponse.responseMessage,
            reviewResponse.responseCreateTime.dateTimeFmt1,
            shopData.shopName
        )
    }

    private fun convertToImageAttachmentViewModel(reviewImageUrl: List<ImageAttachmentDomain>): List<ImageAttachmentUiModel> {
        return reviewImageUrl.map {
            ImageAttachmentUiModel(
                it.attachmentId,
                it.description,
                it.uriThumbnail,
                it.uriLarge
            )
        }
    }

    protected fun convertToReputationViewModel(inboxReputationDomain: InboxReputationDomain): InboxReputationUiModel {
        return InboxReputationUiModel(
            convertToInboxReputationList(inboxReputationDomain.inboxReputation),
            inboxReputationDomain.paging.isHasNext
        )
    }

    private fun convertToInboxReputationList(inboxReputationDomain: List<InboxReputationItemDomain>): List<InboxReputationItemUiModel> {
        return inboxReputationDomain.map {
            InboxReputationItemUiModel(
                it.reputationId.toString(),
                it.revieweeData.revieweeName,
                it.orderData.createTimeFmt,
                it.revieweeData.revieweePicture,
                it.reputationData.lockingDeadlineDays.toString(),
                it.orderData.invoiceRefNum,
                convertToReputationViewModel(it.reputationData),
                it.revieweeData.revieweeRoleId,
                convertToBuyerReputationViewModel(it.revieweeData.revieweeBadgeCustomer),
                convertToSellerReputationViewModel(it.revieweeData.revieweeBadgeSeller),
                it.shopId,
                it.userId
            )
        }
    }

    private fun convertToSellerReputationViewModel(revieweeBadgeSeller: RevieweeBadgeSellerDomain): RevieweeBadgeSellerUiModel {
        return RevieweeBadgeSellerUiModel(
            revieweeBadgeSeller.tooltip,
            revieweeBadgeSeller.reputationScore,
            revieweeBadgeSeller.score,
            revieweeBadgeSeller.minBadgeScore,
            revieweeBadgeSeller.reputationBadgeUrl,
            convertToReputationBadgeViewModel(revieweeBadgeSeller.reputationBadge),
            revieweeBadgeSeller.isFavorited
        )
    }

    private fun convertToBuyerReputationViewModel(
        revieweeBadgeCustomer: RevieweeBadgeCustomerDomain
    ): RevieweeBadgeCustomerUiModel {
        return RevieweeBadgeCustomerUiModel(
            revieweeBadgeCustomer.positive,
            revieweeBadgeCustomer.neutral,
            revieweeBadgeCustomer.negative,
            revieweeBadgeCustomer.positivePercentage,
            revieweeBadgeCustomer.noReputation
        )
    }

    private fun convertToReputationViewModel(reputationData: ReputationDataDomain): ReputationDataUiModel {
        return ReputationDataUiModel(
            reputationData.revieweeScore,
            reputationData.revieweeScoreStatus,
            reputationData.isShowRevieweeScore,
            reputationData.reviewerScore,
            reputationData.reviewerScoreStatus,
            reputationData.isEditable,
            reputationData.isInserted,
            reputationData.isLocked,
            reputationData.isAutoScored,
            reputationData.isCompleted,
            reputationData.isShowLockingDeadline,
            reputationData.lockingDeadlineDays,
            reputationData.isShowBookmark,
            reputationData.actionMessage
        )
    }
}
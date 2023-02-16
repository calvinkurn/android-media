package com.tokopedia.review.feature.inbox.buyerreview.view.subscriber

import android.text.TextUtils
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.ImageAttachmentDomain
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.InboxReputationDetailDomain
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.InboxReputationResponseWrapper
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.ReviewDomain
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.ReviewItemDomain
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.ShopDataDomain
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.VideoAttachmentDomain
import com.tokopedia.review.feature.inbox.buyerreview.view.listener.InboxReputationDetail
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.InboxReputationItemUiModel
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.InboxReputationUiModel
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.ReputationDataUiModel
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail.InboxReputationDetailItemUiModel
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail.ReviewResponseUiModel
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail.RevieweeBadgeCustomerUiModel
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail.RevieweeBadgeSellerUiModel
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.model.Detail
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.domain.model.ProductrevGetReviewMedia
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uimodel.ReviewMediaImageThumbnailUiModel
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uimodel.ReviewMediaThumbnailUiModel
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uimodel.ReviewMediaVideoThumbnailUiModel
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uistate.ReviewMediaImageThumbnailUiState
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uistate.ReviewMediaVideoThumbnailUiState
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
            convertToReputationViewModel(inboxReputationDetailDomain.inboxReputationResponse).list.getOrNull(
                0
            ) ?: InboxReputationItemUiModel(),
            mappingToListItemViewModel(inboxReputationDetailDomain.reviewDomain)
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
        reviewDomain: ReviewDomain,
        itemDomain: ReviewItemDomain
    ): Visitable<*> {
        val reviewMediaThumbnail = convertToReviewMediaThumbnail(itemDomain.reviewData.imageAttachments, itemDomain.reviewData.videoAttachments, itemDomain.feedbackID)
        return InboxReputationDetailItemUiModel(
            reviewDomain.reputationId,
            itemDomain.productData.productId,
            itemDomain.productData.productName,
            itemDomain.productData.productImageUrl,
            itemDomain.productData.productImageUrl,
            itemDomain.reviewData.feedbackID,
            reviewDomain.userData.fullName,
            if (TextUtils.isEmpty(itemDomain.reviewData.reviewUpdateTime)) {
                itemDomain.reviewData.reviewCreateTime
            } else {
                itemDomain.reviewData.reviewUpdateTime
            },
            reviewMediaThumbnail,
            convertToPreloadedDetailedReviewMedia(reviewMediaThumbnail),
            itemDomain.reviewData.reviewMessage,
            itemDomain.reviewData.reviewRating,
            itemDomain.isReviewHasReviewed,
            itemDomain.isReviewIsEditable,
            itemDomain.isReviewIsSkipped,
            reviewDomain.shopData.shopId,
            viewListener.tab,
            convertToReviewResponseViewModel(
                reviewDomain.shopData,
                itemDomain.reviewData.responseMessage,
                itemDomain.reviewData.responseTime
            ),
            itemDomain.reviewData.isReviewAnonymity,
            itemDomain.productData.productStatus == PRODUCT_IS_DELETED,
            !TextUtils.isEmpty(itemDomain.reviewData.reviewUpdateTime),
            reviewDomain.userData.userId,
            itemDomain.productData.productStatus == PRODUCT_IS_BANNED,
            itemDomain.productData.productStatus,
            reviewDomain.orderId
        )
    }

    private fun convertToReviewResponseViewModel(
        shopData: ShopDataDomain,
        responseMessage: String,
        responseTime: String
    ): ReviewResponseUiModel {
        return ReviewResponseUiModel(
            responseMessage,
            responseTime,
            shopData.shopName
        )
    }

    private fun convertToReviewMediaThumbnail(
        imageAttachments: List<ImageAttachmentDomain>,
        videoAttachments: List<VideoAttachmentDomain>,
        reviewId: String
    ): ReviewMediaThumbnailUiModel {
        val thumbnails = videoAttachments.map { videoAttachment ->
            ReviewMediaVideoThumbnailUiModel(
                ReviewMediaVideoThumbnailUiState.Showing(
                    attachmentID = videoAttachment.attachmentId,
                    reviewID = reviewId,
                    url = videoAttachment.videoUrl
                )
            )
        }.plus(
            imageAttachments.map { imageAttachments ->
                ReviewMediaImageThumbnailUiModel(
                    ReviewMediaImageThumbnailUiState.Showing(
                        attachmentID = imageAttachments.attachmentId,
                        reviewID = reviewId,
                        thumbnailUrl = imageAttachments.uriThumbnail,
                        fullSizeUrl = imageAttachments.uriLarge
                    )
                )
            }
        )
        return ReviewMediaThumbnailUiModel(thumbnails)
    }

    private fun convertToPreloadedDetailedReviewMedia(
        reviewMediaThumbnailUiModel: ReviewMediaThumbnailUiModel
    ): ProductrevGetReviewMedia {
        return ProductrevGetReviewMedia(
            reviewMedia = reviewMediaThumbnailUiModel.generateReviewMedia(),
            detail = Detail(
                reviewGalleryImages = reviewMediaThumbnailUiModel.generateReviewGalleryImage(),
                reviewGalleryVideos = reviewMediaThumbnailUiModel.generateReviewGalleryVideo(),
                mediaCountFmt = reviewMediaThumbnailUiModel.generateMediaCount().toString(),
                mediaCount = reviewMediaThumbnailUiModel.generateMediaCount()
            )
        )
    }

    protected fun convertToReputationViewModel(inboxReputationResponse: InboxReputationResponseWrapper.Data.Response): InboxReputationUiModel {
        return InboxReputationUiModel(
            convertToInboxReputationList(inboxReputationResponse.reputationList),
            inboxReputationResponse.hasNext
        )
    }

    private fun convertToInboxReputationList(reputationList: List<InboxReputationResponseWrapper.Data.Response.Reputation>): List<InboxReputationItemUiModel> {
        return reputationList.map {
            InboxReputationItemUiModel(
                it.reputationIdStr,
                it.revieweeData.name,
                it.orderData.createTimeFmt,
                it.revieweeData.picture,
                it.reputationData.lockingDeadlineDays.toString(),
                it.orderData.invoiceRefNum,
                convertToReputationViewModel(it.reputationData),
                it.revieweeData.roleId,
                convertToBuyerReputationViewModel(it.revieweeData.buyerBadge),
                convertToSellerReputationViewModel(it.revieweeData.shopBadge),
                it.shopIdStr,
                it.userIdStr
            )
        }
    }

    private fun convertToSellerReputationViewModel(revieweeBadgeSeller: InboxReputationResponseWrapper.Data.Response.Reputation.RevieweeData.ShopBadge): RevieweeBadgeSellerUiModel {
        return RevieweeBadgeSellerUiModel(
            revieweeBadgeSeller.tooltip,
            revieweeBadgeSeller.reputationScore,
            revieweeBadgeSeller.score,
            revieweeBadgeSeller.minBadgeScore,
            revieweeBadgeSeller.reputationBadgeUrl,
            revieweeBadgeSeller.isFavorited
        )
    }

    private fun convertToBuyerReputationViewModel(
        revieweeBadgeCustomer: InboxReputationResponseWrapper.Data.Response.Reputation.RevieweeData.BuyerBadge
    ): RevieweeBadgeCustomerUiModel {
        return RevieweeBadgeCustomerUiModel(
            revieweeBadgeCustomer.positive,
            revieweeBadgeCustomer.neutral,
            revieweeBadgeCustomer.negative,
            revieweeBadgeCustomer.positivePercentage,
            revieweeBadgeCustomer.noReputation
        )
    }

    private fun convertToReputationViewModel(reputationData: InboxReputationResponseWrapper.Data.Response.Reputation.ReputationData): ReputationDataUiModel {
        return ReputationDataUiModel(
            reputationData.revieweeScore,
            reputationData.revieweeScoreStatus,
            reputationData.showRevieweeScore,
            reputationData.reviewerScore,
            reputationData.reviewerScoreStatus,
            reputationData.isEditable,
            reputationData.isInserted,
            reputationData.isLocked,
            reputationData.isAutoScored,
            reputationData.isCompleted,
            reputationData.showLockingDeadline,
            reputationData.lockingDeadlineDays,
            reputationData.showBookmark,
            reputationData.actionMessage
        )
    }
}

package com.tokopedia.review.feature.inboxreview.domain.mapper

import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.review.common.util.ReviewConstants.ALL_RATINGS
import com.tokopedia.review.common.util.ReviewConstants.ANSWERED_KEY
import com.tokopedia.review.common.util.ReviewConstants.ANSWERED_VALUE
import com.tokopedia.review.common.util.ReviewConstants.UNANSWERED_KEY
import com.tokopedia.review.common.util.ReviewConstants.UNANSWERED_VALUE
import com.tokopedia.review.common.util.ReviewConstants.prefixStatus
import com.tokopedia.review.common.util.getStatusFilter
import com.tokopedia.review.common.util.isAnswered
import com.tokopedia.review.feature.inboxreview.domain.response.InboxReviewResponse
import com.tokopedia.review.feature.inboxreview.presentation.model.FeedbackInboxUiModel
import com.tokopedia.review.feature.inboxreview.presentation.model.InboxReviewUiModel
import com.tokopedia.review.feature.inboxreview.presentation.model.ListItemRatingWrapper
import com.tokopedia.review.feature.inboxreview.presentation.model.SortFilterInboxItemWrapper
import com.tokopedia.review.feature.reviewdetail.view.model.FeedbackUiModel
import com.tokopedia.review.feature.reviewdetail.view.model.RatingBarUiModel
import com.tokopedia.review.feature.reviewdetail.view.model.SortFilterItemWrapper
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uimodel.ReviewMediaImageThumbnailUiModel
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uimodel.ReviewMediaThumbnailUiModel
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uimodel.ReviewMediaVideoThumbnailUiModel
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uistate.ReviewMediaImageThumbnailUiState
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uistate.ReviewMediaVideoThumbnailUiState
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.user.session.UserSessionInterface

object InboxReviewMapper {

    private const val REPLY_LENGTH_THRESHOLD = 150
    private const val MIN_RATING_FILTER = 1
    private const val MAX_RATING_FILTER = 5

    fun mapToInboxReviewUiModel(
        inboxReviewResponse: InboxReviewResponse.ProductGetInboxReviewByShop,
        userSession: UserSessionInterface
    ): InboxReviewUiModel {
        return InboxReviewUiModel(
            feedbackInboxList = mapToFeedbackUiModel(inboxReviewResponse, userSession),
            filterBy = inboxReviewResponse.filterBy.orEmpty(),
            page = inboxReviewResponse.page.orZero(),
            hasNext = inboxReviewResponse.hasNext ?: false,
            remainder = inboxReviewResponse.remainder.orZero(),
            useAutoReply = inboxReviewResponse.useAutoReply ?: false
        )
    }

    fun mapToFeedbackUiModel(
        inboxReviewResponse: InboxReviewResponse.ProductGetInboxReviewByShop,
        userSession: UserSessionInterface
    ): List<FeedbackInboxUiModel> {
        val feedbackListUiModel = mutableListOf<FeedbackInboxUiModel>()
        inboxReviewResponse.list.map {
            feedbackListUiModel.add(
                FeedbackInboxUiModel(
                    reviewMediaThumbnail = ReviewMediaThumbnailUiModel(
                        mediaThumbnails = it.videoAttachments.map { videoAttachment ->
                            ReviewMediaVideoThumbnailUiModel(
                                uiState = ReviewMediaVideoThumbnailUiState.Showing(
                                    attachmentID = videoAttachment.attachmentID.orEmpty(),
                                    reviewID = it.feedbackID,
                                    url = videoAttachment.videoUrl.orEmpty()
                                )
                            )
                        }.plus(
                            it.imageAttachments.map { imageAttachment ->
                                ReviewMediaImageThumbnailUiModel(
                                    uiState = ReviewMediaImageThumbnailUiState.Showing(
                                        attachmentID = imageAttachment.attachmentID.orEmpty(),
                                        reviewID = it.feedbackID,
                                        thumbnailUrl = imageAttachment.thumbnailURL.orEmpty(),
                                        fullSizeUrl = imageAttachment.thumbnailURL.orEmpty()
                                    )
                                )
                            }
                        )
                    ),
                    isAutoReply = it.isAutoReply ?: false,
                    feedbackId = it.feedbackID,
                    rating = it.rating.orZero(),
                    replyText = it.replyText.orEmpty(),
                    replyTime = it.replyTime.orEmpty(),
                    reviewText = it.reviewText.orEmpty(),
                    isMoreReply = it.replyText?.length.orZero() >= REPLY_LENGTH_THRESHOLD,
                    reviewTime = it.reviewTime.orEmpty(),
                    userID = it.user.userID,
                    username = it.user.userName.orEmpty(),
                    productID = it.product.productID,
                    productName = it.product.productName.orEmpty(),
                    productImageUrl = it.product.productImageURL.orEmpty(),
                    variantID = it.product.productVariant.variantID,
                    variantName = it.product.productVariant.variantName.orEmpty(),
                    invoiceID = it.invoiceID.orEmpty(),
                    sellerUser = userSession.name,
                    isReplied = inboxReviewResponse.filterBy?.getStatusFilter(prefixStatus)?.isAnswered
                        ?: false,
                    isKejarUlasan = it.isKejarUlasan,
                    ratingDisclaimer = it.ratingDisclaimer,
                    badRatingReason = it.badRatingReasonFmt
                )
            )
        }
        return feedbackListUiModel
    }

    fun mapToItemSortFilterIsEmpty(): ArrayList<SortFilterInboxItemWrapper> {
        val itemSortFilterList = ArrayList<SortFilterInboxItemWrapper>()

        val sortFilter = SortFilterItem(
            title = ALL_RATINGS,
            type = ChipsUnify.TYPE_NORMAL,
            size = ChipsUnify.SIZE_SMALL
        )

        itemSortFilterList.add(SortFilterInboxItemWrapper(sortFilter, isSelected = false))

        val sortFilterUnAnswered = SortFilterItem(
            title = UNANSWERED_KEY,
            type = ChipsUnify.TYPE_NORMAL,
            size = ChipsUnify.SIZE_SMALL
        )

        itemSortFilterList.add(
            SortFilterInboxItemWrapper(
                sortFilterUnAnswered,
                sortValue = UNANSWERED_VALUE,
                isSelected = false
            )
        )

        val sortFilterAnswered = SortFilterItem(
            title = ANSWERED_KEY,
            type = ChipsUnify.TYPE_NORMAL,
            size = ChipsUnify.SIZE_SMALL
        )

        itemSortFilterList.add(
            SortFilterInboxItemWrapper(
                sortFilterAnswered,
                sortValue = ANSWERED_VALUE
            )
        )

        return itemSortFilterList
    }

    fun mapToItemRatingFilterBottomSheet(): ArrayList<ListItemRatingWrapper> {
        val itemUnifyList: ArrayList<ListItemRatingWrapper> = arrayListOf()

        itemUnifyList.apply {
            for (i in MIN_RATING_FILTER..MAX_RATING_FILTER) {
                add(
                    ListItemRatingWrapper(
                        isSelected = false,
                        sortValue = i.toString()
                    )
                )
            }
        }

        return itemUnifyList
    }

    fun mapFeedbackInboxToFeedbackUiModel(data: FeedbackInboxUiModel): FeedbackUiModel {
        return FeedbackUiModel(
            reviewMediaThumbnail = data.reviewMediaThumbnail,
            autoReply = data.isAutoReply,
            feedbackID = data.feedbackId,
            productID = data.productID,
            rating = data.rating,
            replyText = data.replyText,
            replyTime = data.replyTime,
            reviewText = data.reviewText,
            isMoreReply = data.isMoreReply,
            reviewTime = data.reviewTime,
            reviewerName = data.username,
            variantName = data.variantName,
            sellerUser = data.sellerUser,
            isKejarUlasan = data.isKejarUlasan,
            badRatingReason = data.badRatingReason,
            badRatingDisclaimer = data.ratingDisclaimer
        )
    }

    fun mapToStatusFilterList(data: List<SortFilterInboxItemWrapper>): List<SortFilterInboxItemWrapper> {
        val statusList = mutableListOf<SortFilterInboxItemWrapper>()
        data.mapIndexed { index, sortFilter ->
            if (index > 0) {
                statusList.add(sortFilter)
            }
        }
        return statusList
    }

    fun mapToUnSelectedRatingList(data: List<ListItemRatingWrapper>): ArrayList<ListItemRatingWrapper> {
        return ArrayList(data.map {
            it.isSelected = false
            it
        })
    }

    fun mapToUnSelectedStatusList(data: List<SortFilterInboxItemWrapper>): List<SortFilterInboxItemWrapper> {
        return data.map {
            it.isSelected = false
            it
        }
    }

    fun mapToUnSelectedSortAndFilter(data: Pair<List<SortFilterItemWrapper>, String>): Pair<List<SortFilterItemWrapper>, String> {
        val updatedData = data.first.map {
            if (it.count == 0) {
                it.isSelected = false
            }
            it
        }
        return Pair(updatedData, data.second)
    }

    fun mapToUnSelectedFilterTopic(data: List<SortFilterItemWrapper>): List<SortFilterItemWrapper> {
        return data.map {
            if (it.count == 0) {
                it.isSelected = false
            }
            it
        }
    }

    fun mapToUnSelectedFilterRating(data: List<RatingBarUiModel>): List<RatingBarUiModel> {
        return data.map {
            if (it.ratingCount == 0) {
                it.ratingIsChecked = false
            }
            it
        }
    }


}
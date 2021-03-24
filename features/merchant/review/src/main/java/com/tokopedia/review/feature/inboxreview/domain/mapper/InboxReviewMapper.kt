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
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.user.session.UserSessionInterface

object InboxReviewMapper {

    fun mapToInboxReviewUiModel(inboxReviewResponse: InboxReviewResponse.ProductGetInboxReviewByShop,
                                userSession: UserSessionInterface): InboxReviewUiModel {
        return InboxReviewUiModel(
                feedbackInboxList = mapToFeedbackUiModel(inboxReviewResponse, userSession),
                filterBy = inboxReviewResponse.filterBy.orEmpty(),
                page = inboxReviewResponse.page.orZero(),
                hasNext = inboxReviewResponse.hasNext ?: false,
                remainder = inboxReviewResponse.remainder.orZero(),
                useAutoReply = inboxReviewResponse.useAutoReply ?: false
        )
    }

    fun mapToFeedbackUiModel(inboxReviewResponse: InboxReviewResponse.ProductGetInboxReviewByShop,
                             userSession: UserSessionInterface): List<FeedbackInboxUiModel> {
        val feedbackListUiModel = mutableListOf<FeedbackInboxUiModel>()
        inboxReviewResponse.list.map {
            val mapAttachment = mutableListOf<FeedbackInboxUiModel.Attachment>()
            it.attachments.map { attachment ->
                mapAttachment.add(FeedbackInboxUiModel.Attachment(
                        thumbnailURL = attachment.thumbnailURL.orEmpty(),
                        fullSizeURL = attachment.fullSizeURL.orEmpty()
                ))
            }
            feedbackListUiModel.add(
                    FeedbackInboxUiModel(
                            attachments = mapAttachment,
                            isAutoReply = it.isAutoReply ?: false,
                            feedbackId = it.feedbackID.orZero(),
                            rating = it.rating.orZero(),
                            replyText = it.replyText.orEmpty(),
                            replyTime = it.replyTime.orEmpty(),
                            reviewText = it.reviewText.orEmpty(),
                            isMoreReply = it.replyText?.length.orZero() >= 150,
                            reviewTime = it.reviewTime.orEmpty(),
                            userID = it.user.userID.orZero(),
                            username = it.user.userName.orEmpty(),
                            productID = it.product.productID.orZero(),
                            productName = it.product.productName.orEmpty(),
                            productImageUrl = it.product.productImageURL.orEmpty(),
                            variantID = it.product.productVariant.variantID.orZero(),
                            variantName = it.product.productVariant.variantName.orEmpty(),
                            invoiceID = it.invoiceID.orEmpty(),
                            sellerUser = userSession.name,
                            isReplied = inboxReviewResponse.filterBy?.getStatusFilter(prefixStatus)?.isAnswered
                                    ?: false,
                            isKejarUlasan = it.isKejarUlasan
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
                size = ChipsUnify.SIZE_SMALL)

        itemSortFilterList.add(SortFilterInboxItemWrapper(sortFilter, isSelected = false))

        val sortFilterUnAnswered = SortFilterItem(
                title = UNANSWERED_KEY,
                type = ChipsUnify.TYPE_NORMAL,
                size = ChipsUnify.SIZE_SMALL)

        itemSortFilterList.add(SortFilterInboxItemWrapper(sortFilterUnAnswered, sortValue = UNANSWERED_VALUE, isSelected = false))

        val sortFilterAnswered = SortFilterItem(
                title = ANSWERED_KEY,
                type = ChipsUnify.TYPE_NORMAL,
                size = ChipsUnify.SIZE_SMALL)

        itemSortFilterList.add(SortFilterInboxItemWrapper(sortFilterAnswered, sortValue = ANSWERED_VALUE))

        return itemSortFilterList
    }

    fun mapToItemRatingFilterBottomSheet(): ArrayList<ListItemRatingWrapper> {
        val itemUnifyList: ArrayList<ListItemRatingWrapper> = arrayListOf()
        val maxRatingFilter = 5

        itemUnifyList.apply {
            for (i in 1..maxRatingFilter) {
                add(ListItemRatingWrapper(
                        isSelected = false,
                        sortValue = i.toString()
                ))
            }
        }

        return itemUnifyList
    }

    fun mapToItemImageSlider(attachmentList: List<FeedbackInboxUiModel.Attachment>?): Pair<List<String>, List<String>> {
        val imageSlider = arrayListOf<String>()
        val thumbnailSlider = arrayListOf<String>()

        attachmentList?.map {
            thumbnailSlider.add(it.thumbnailURL)
            imageSlider.add(it.fullSizeURL)
        }

        return Pair(thumbnailSlider, imageSlider)
    }

    fun mapFeedbackInboxToFeedbackUiModel(data: FeedbackInboxUiModel): FeedbackUiModel {
        val mapAttachment = mutableListOf<FeedbackUiModel.Attachment>()
        data.attachments.map { attachment ->
            mapAttachment.add(FeedbackUiModel.Attachment(
                    thumbnailURL = attachment.thumbnailURL,
                    fullSizeURL = attachment.fullSizeURL
            ))
        }
        return FeedbackUiModel(
                attachments = mapAttachment,
                autoReply = data.isAutoReply,
                feedbackID = data.feedbackId,
                rating = data.rating,
                replyText = data.replyText,
                replyTime = data.replyTime,
                reviewText = data.reviewText,
                isMoreReply = data.isMoreReply,
                reviewTime = data.reviewTime,
                reviewerName = data.username,
                variantName = data.variantName,
                sellerUser = data.sellerUser,
                isKejarUlasan = data.isKejarUlasan
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
        val updatedData =  data.first.map {
            if (it.count == 0) {
                it.isSelected = false
            }
            it
        }
        return Pair(updatedData, data.second)
    }

    fun mapToUnSelectedFilterTopic(data: List<SortFilterItemWrapper>): List<SortFilterItemWrapper> {
        val updatedData = data.map {
            if (it.count == 0) {
                it.isSelected = false
            }
            it
        }
        return updatedData
    }

    fun mapToUnSelectedFilterRating(data: List<RatingBarUiModel>): List<RatingBarUiModel> {
        val updatedData = data.map {
            if (it.ratingCount == 0) {
                it.ratingIsChecked = false
            }
            it
        }
        return updatedData
    }



}
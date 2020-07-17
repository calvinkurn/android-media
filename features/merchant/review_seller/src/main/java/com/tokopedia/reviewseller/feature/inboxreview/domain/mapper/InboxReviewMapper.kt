package com.tokopedia.reviewseller.feature.inboxreview.domain.mapper

import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.reviewseller.R
import com.tokopedia.reviewseller.common.util.ReviewSellerConstant.ALL_RATINGS
import com.tokopedia.reviewseller.common.util.ReviewSellerConstant.ANSWERED_KEY
import com.tokopedia.reviewseller.common.util.ReviewSellerConstant.ANSWERED_VALUE
import com.tokopedia.reviewseller.common.util.ReviewSellerConstant.UNANSWERED_KEY
import com.tokopedia.reviewseller.common.util.ReviewSellerConstant.UNANSWERED_VALUE
import com.tokopedia.reviewseller.common.util.ReviewSellerConstant.prefixStatus
import com.tokopedia.reviewseller.common.util.getStatusFilter
import com.tokopedia.reviewseller.common.util.isAnswered
import com.tokopedia.reviewseller.feature.inboxreview.domain.response.InboxReviewResponse
import com.tokopedia.reviewseller.feature.inboxreview.presentation.model.FeedbackInboxUiModel
import com.tokopedia.reviewseller.feature.inboxreview.presentation.model.InboxReviewUiModel
import com.tokopedia.reviewseller.feature.inboxreview.presentation.model.ListItemRatingWrapper
import com.tokopedia.reviewseller.feature.inboxreview.presentation.model.SortFilterInboxItemWrapper
import com.tokopedia.reviewseller.feature.reviewdetail.view.model.FeedbackUiModel
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.setImage
import com.tokopedia.user.session.UserSessionInterface
import java.util.*

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
                                    ?: false
                    )
            )
        }
        return feedbackListUiModel
    }

    fun mapToItemSortFilterIsNotEmpty(countSelected: Int, isAllFilter: Boolean, data: List<SortFilterInboxItemWrapper>): ArrayList<SortFilterInboxItemWrapper> {
        val itemSortFilterList = ArrayList<SortFilterInboxItemWrapper>()

        data.mapIndexed { index, sortFilterItem ->
            if (index == 0) {
                if (!isAllFilter) {
                    if (countSelected == 0) {
                        val sortFilter = SortFilterItem(
                                title = ALL_RATINGS,
                                type = if (sortFilterItem.isSelected) ChipsUnify.TYPE_SELECTED else ChipsUnify.TYPE_NORMAL,
                                size = ChipsUnify.SIZE_SMALL).apply {
                            refChipUnify.setChevronClickListener { }
                        }
                        itemSortFilterList.add(SortFilterInboxItemWrapper(sortFilter, sortValue = "", isSelected = sortFilterItem.isSelected))
                    } else if (countSelected > 1) {
                        val sortFilter = SortFilterItem(
                                title = "($countSelected) Filter",
                                type = if (sortFilterItem.isSelected) ChipsUnify.TYPE_SELECTED else ChipsUnify.TYPE_NORMAL,
                                size = ChipsUnify.SIZE_SMALL).apply {
                            refChipUnify.setChevronClickListener { }
                        }
                        itemSortFilterList.add(SortFilterInboxItemWrapper(sortFilter, sortValue = sortFilterItem.sortValue, isSelected = sortFilterItem.isSelected))
                    } else {
                        val sortFilter = SortFilterItem(
                                title = countSelected.toString(),
                                type = if (sortFilterItem.isSelected) ChipsUnify.TYPE_SELECTED else ChipsUnify.TYPE_NORMAL,
                                size = ChipsUnify.SIZE_SMALL).apply {
                            refChipUnify.chip_image_icon.setImage(R.drawable.ic_rating_star_item, 0F)
                            refChipUnify.setChevronClickListener { }
                        }
                        itemSortFilterList.add(SortFilterInboxItemWrapper(sortFilter, sortValue = sortFilterItem.sortValue, isSelected = sortFilterItem.isSelected))
                    }
                } else {
                    val sortFilter = SortFilterItem(
                            title = ALL_RATINGS,
                            type = if (sortFilterItem.isSelected) ChipsUnify.TYPE_SELECTED else ChipsUnify.TYPE_NORMAL,
                            size = ChipsUnify.SIZE_SMALL).apply {
                        refChipUnify.setChevronClickListener { }
                    }
                    val allSort = "all"
                    itemSortFilterList.add(SortFilterInboxItemWrapper(sortFilter, sortValue = allSort, isSelected = sortFilterItem.isSelected))
                }
            }

            val sortFilterUnAnswered = SortFilterItem(
                    title = sortFilterItem.sortFilterItem?.title.toString(),
                    type = if (sortFilterItem.isSelected) ChipsUnify.TYPE_SELECTED else ChipsUnify.TYPE_NORMAL,
                    size = ChipsUnify.SIZE_SMALL)

            itemSortFilterList.add(SortFilterInboxItemWrapper(sortFilterUnAnswered, sortValue = sortFilterItem.sortValue,
                    isSelected = sortFilterItem.isSelected))
        }

        return itemSortFilterList
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

    fun mapToItemRatingFilterBottomSheet(data: ArrayList<ListItemRatingWrapper>): ArrayList<ListItemRatingWrapper> {
        val itemUnifyList: ArrayList<ListItemRatingWrapper> = arrayListOf()
        val maxRatingFilter = 5

        if (data.isEmpty()) {
            itemUnifyList.apply {
                for (i in 1..maxRatingFilter) {
                    add(ListItemRatingWrapper(
                            isSelected = false,
                            sortValue = i.toString()
                    ))
                }
            }
        } else {
            itemUnifyList.apply {
                data.map { item ->
                    add(
                            ListItemRatingWrapper(
                                    isSelected = item.isSelected,
                                    sortValue = item.sortValue
                            )
                    )
                }
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
                sellerUser = data.sellerUser
        )
    }

}
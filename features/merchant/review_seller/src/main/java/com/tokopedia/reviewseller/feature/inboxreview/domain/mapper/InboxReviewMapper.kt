package com.tokopedia.reviewseller.feature.inboxreview.domain.mapper

import android.content.Context
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.reviewseller.R
import com.tokopedia.reviewseller.common.util.ReviewSellerConstant.ALL_RATINGS
import com.tokopedia.reviewseller.common.util.ReviewSellerConstant.ANSWERED_KEY
import com.tokopedia.reviewseller.common.util.ReviewSellerConstant.ANSWERED_VALUE
import com.tokopedia.reviewseller.common.util.ReviewSellerConstant.UNANSWERED_KEY
import com.tokopedia.reviewseller.common.util.ReviewSellerConstant.UNANSWERED_VALUE
import com.tokopedia.reviewseller.feature.inboxreview.domain.response.InboxReviewResponse
import com.tokopedia.reviewseller.feature.inboxreview.presentation.model.*
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.list.ListItemUnify
import com.tokopedia.unifycomponents.setImage
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.user.session.UserSessionInterface

object InboxReviewMapper {

    fun mapToInboxReviewUiModel(inboxReviewResponse: InboxReviewResponse.ProductGetInboxReviewByShop,
                                itemRatingListWrapper: ArrayList<ListItemRatingWrapper>,
                                userSession: UserSessionInterface): InboxReviewUiModel {
        return InboxReviewUiModel(
                feedbackInboxList = mapToFeedbackUiModel(inboxReviewResponse, userSession),
                filterItemWrapper = FilterInboxReviewUiModel(mapToItemSortFilter(itemRatingListWrapper)),
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
                            variantID = it.product.productVariant.variantID.orZero(),
                            variantName = it.product.productVariant.variantName.orEmpty(),
                            invoiceID = it.invoiceID.orEmpty(),
                            sellerUser = userSession.name
                    )
            )
        }
        return feedbackListUiModel
    }

    fun mapToItemSortFilter(data: ArrayList<ListItemRatingWrapper>): ArrayList<SortFilterInboxItemWrapper> {
        val countSelected = data.filter { it.isSelected }.count()
        val valueSelected = data.joinToString(separator = ",") { it.sortValue }
        val isAllFilter = countSelected == 5

        return if (data.isEmpty()) {
            mapToItemSortFilterIsEmpty(countSelected, valueSelected = valueSelected, isAllFilter = isAllFilter)
        } else {
            mapToItemSortFilterIsNotEmpty(countSelected, valueSelected, isAllFilter, data)
        }
    }

    fun mapToItemSortFilterIsNotEmpty(countSelected: Int, valueSelected: String, isAllFilter: Boolean, data: ArrayList<ListItemRatingWrapper>): ArrayList<SortFilterInboxItemWrapper> {
        val itemSortFilterList = ArrayList<SortFilterInboxItemWrapper>()

        data.mapIndexed { index, listItemRatingWrapper ->
            if (index == 0) {
                if (!isAllFilter) {
                    if (countSelected > 1) {
                        val sortFilter = SortFilterItem(
                                title = "($countSelected) Filter",
                                type = if(listItemRatingWrapper.isSelected) ChipsUnify.TYPE_SELECTED else ChipsUnify.TYPE_NORMAL,
                                size = ChipsUnify.SIZE_SMALL).apply {
                            refChipUnify.setChevronClickListener { }
                        }
                        itemSortFilterList.add(SortFilterInboxItemWrapper(sortFilter, sortValue = valueSelected, isSelected = listItemRatingWrapper.isSelected))
                    } else {
                        val sortFilter = SortFilterItem(
                                title = countSelected.toString(),
                                type = if(listItemRatingWrapper.isSelected) ChipsUnify.TYPE_SELECTED else ChipsUnify.TYPE_NORMAL,
                                size = ChipsUnify.SIZE_SMALL).apply {
                            refChipUnify.chip_image_icon.setImage(R.drawable.ic_rating_star_item, 0F)
                            refChipUnify.setChevronClickListener { }
                        }
                        itemSortFilterList.add(SortFilterInboxItemWrapper(sortFilter, sortValue = valueSelected, isSelected = listItemRatingWrapper.isSelected))
                    }
                } else {
                    val sortFilter = SortFilterItem(
                            title = ALL_RATINGS,
                            type = if(listItemRatingWrapper.isSelected) ChipsUnify.TYPE_SELECTED else ChipsUnify.TYPE_NORMAL,
                            size = ChipsUnify.SIZE_SMALL).apply {
                        refChipUnify.setChevronClickListener { }
                    }
                    val allSort = "all"
                    itemSortFilterList.add(SortFilterInboxItemWrapper(sortFilter, sortValue = allSort, isSelected = listItemRatingWrapper.isSelected))
                }
            }

            val sortFilterUnAnswered = SortFilterItem(
                    title = listItemRatingWrapper.listItemUnify?.listTitleText.orEmpty(),
                    type = ChipsUnify.TYPE_NORMAL,
                    size = ChipsUnify.SIZE_SMALL)

            itemSortFilterList.add(SortFilterInboxItemWrapper(sortFilterUnAnswered, sortValue = listItemRatingWrapper.sortValue,
                    isSelected = listItemRatingWrapper.isSelected))
        }

        return itemSortFilterList
    }

    fun mapToItemSortFilterIsEmpty(countSelected: Int, valueSelected: String, isAllFilter: Boolean): ArrayList<SortFilterInboxItemWrapper> {
        val itemSortFilterList = ArrayList<SortFilterInboxItemWrapper>()

        if (!isAllFilter) {
            if (countSelected > 1) {
                val sortFilter = SortFilterItem(
                        title = "($countSelected) Filter",
                        type = ChipsUnify.TYPE_NORMAL,
                        size = ChipsUnify.SIZE_SMALL).apply {
                    refChipUnify.setChevronClickListener { }
                }
                itemSortFilterList.add(SortFilterInboxItemWrapper(sortFilter, sortValue = valueSelected, isSelected = false))
            } else {
                val sortFilter = SortFilterItem(
                        title = countSelected.toString(),
                        type = ChipsUnify.TYPE_NORMAL,
                        size = ChipsUnify.SIZE_SMALL).apply {
                    refChipUnify.chip_image_icon.setImage(R.drawable.ic_rating_star_item, 0F)
                    refChipUnify.setChevronClickListener { }
                }
                itemSortFilterList.add(SortFilterInboxItemWrapper(sortFilter, sortValue = valueSelected, isSelected = false))
            }
        } else {
            val sortFilter = SortFilterItem(
                    title = ALL_RATINGS,
                    type = ChipsUnify.TYPE_NORMAL,
                    size = ChipsUnify.SIZE_SMALL).apply {
                refChipUnify.setChevronClickListener { }
            }
            val allSort = "all"
            itemSortFilterList.add(SortFilterInboxItemWrapper(sortFilter, sortValue = allSort, isSelected = false))
        }

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

    fun mapToItemRatingFilterBottomSheet(context: Context, data: ArrayList<ListItemRatingWrapper>): ArrayList<ListItemRatingWrapper> {
        val itemUnifyList: ArrayList<ListItemRatingWrapper> = arrayListOf()
        val maxRatingFilter = 5
        val iconSize = 20.toPx()
        val iconList = ContextCompat.getDrawable(context, R.drawable.ic_rating_star_item)

        if (data.isEmpty()) {
            itemUnifyList.apply {
                for (i in 1..maxRatingFilter) {
                    add(ListItemRatingWrapper(
                            listItemUnify = ListItemUnify(
                                    title = i.toString(),
                                    description = "").apply {
                                listDrawable = iconList
                                listIconHeight = iconSize
                                listIconWidth = iconSize
                                setVariant(leftComponent = ListItemUnify.CHECKBOX)
                            },
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
                                    listItemUnify = ListItemUnify(
                                            title = item.listItemUnify?.listTitleText.orEmpty(),
                                            description = "").apply {
                                        listDrawable = iconList
                                        listIconHeight = iconSize
                                        listIconWidth = iconSize
                                        setVariant(leftComponent = ListItemUnify.CHECKBOX)
                                    },
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

}
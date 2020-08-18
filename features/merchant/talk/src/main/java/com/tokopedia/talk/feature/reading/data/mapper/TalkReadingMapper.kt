package com.tokopedia.talk.feature.reading.data.mapper

import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.talk.feature.reading.data.model.*
import com.tokopedia.talk.feature.reading.data.model.discussionaggregate.DiscussionAggregate
import com.tokopedia.talk.feature.reading.data.model.discussionaggregate.DiscussionAggregateCategory
import com.tokopedia.talk.feature.reading.data.model.discussionaggregate.DiscussionAggregateResponse
import com.tokopedia.talk.feature.reading.data.model.discussiondata.DiscussionDataResponse
import com.tokopedia.talk.feature.reading.presentation.adapter.uimodel.TalkReadingHeaderModel
import com.tokopedia.talk.feature.reading.presentation.adapter.uimodel.TalkReadingUiModel
import com.tokopedia.talk.feature.reading.presentation.widget.OnCategoryModifiedListener
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.list.ListItemUnify

object TalkReadingMapper {

    const val SORT_CATEGORY = "Paling Relevan"
    const val SORT_LATEST = "Terbaru"
    const val CATEGORY_FILTER_FORMAT = "%s (%d)"

    fun mapDiscussionAggregateResponseToTalkReadingHeaderModel(discussionAggregate: DiscussionAggregate,
                                                               showBottomSheet: () -> Unit,
                                                               onCategoryModifiedListener: OnCategoryModifiedListener): TalkReadingHeaderModel {
        return TalkReadingHeaderModel(
                discussionAggregate.productName,
                discussionAggregate.thumbnail,
                discussionAggregate.category.mapToSortFilter(showBottomSheet, onCategoryModifiedListener)
        )
    }

    fun mapDiscussionDataResponseToTalkReadingUiModel(discussionDataResponse: DiscussionDataResponse, currentUserId: String): List<TalkReadingUiModel> {
        val result = mutableListOf<TalkReadingUiModel>()
        discussionDataResponse.question.forEach {
            result.add(TalkReadingUiModel(it, discussionDataResponse.shopID, currentUserId == it.userId.toString()))
        }
        return result
    }

    fun mapSortOptionsToListUnifyItems(sortOptions: List<SortOption>): ArrayList<ListItemUnify> {
        val result = arrayListOf<ListItemUnify>()
        sortOptions.forEach {
            val itemToAdd = ListItemUnify(title = it.displayName, description = "")
            itemToAdd.setVariant(rightComponent = ListItemUnify.RADIO_BUTTON)
            result.add(itemToAdd)
        }
        return result
    }

    fun mapSelectedSortToSortFilterItem(selectedSortOption: SortOption): String {
        return when(selectedSortOption) {
            is SortOption.SortByInformativeness -> SORT_CATEGORY
            else -> SORT_LATEST
        }
    }

    fun mapSelectedSortToString(selectedSortOption: SortOption?): String {
        return selectedSortOption?.id?.name?.toLowerCase() ?: ""
    }

    fun mapDiscussionAggregateResponseToTalkReadingCategories(discussionAggregateResponse: DiscussionAggregateResponse): List<TalkReadingCategory> {
        val result = mutableListOf<TalkReadingCategory>()
        discussionAggregateResponse.discussionAggregateResponse.category.forEach {
            result.add(TalkReadingCategory(it.name, it.text, false))
        }
        return result
    }

    private fun List<DiscussionAggregateCategory>.mapToSortFilter(showBottomSheet: () -> Unit,
                                                                  onCategoryModifiedListener: OnCategoryModifiedListener): ArrayList<SortFilterItem> {
        val result = arrayListOf<SortFilterItem>()
        val sortChip = SortFilterItem(title = SORT_LATEST, listener = showBottomSheet)
        result.add(sortChip)
        this.forEach {
            val sortFilterItem = SortFilterItem(String.format(CATEGORY_FILTER_FORMAT, it.text, it.counter))
            sortFilterItem.setChipListener(onCategoryModifiedListener)
            result.add(sortFilterItem)
        }
        return result
    }

    private fun SortFilterItem.setChipListener(onCategoryModifiedListener: OnCategoryModifiedListener) {
        this.listener = {
            this.toggle()
            onCategoryModifiedListener.onCategorySelected(this.title.toString(), this.type)
        }
    }

    private fun SortFilterItem.toggle() {
        type = if(type == ChipsUnify.TYPE_NORMAL) {
            ChipsUnify.TYPE_SELECTED
        } else {
            ChipsUnify.TYPE_NORMAL
        }
    }


}
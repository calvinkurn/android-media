package com.tokopedia.talk.feature.reading.data.mapper

import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.talk.feature.reading.data.model.DiscussionAggregate
import com.tokopedia.talk.feature.reading.data.model.DiscussionAggregateCategory
import com.tokopedia.talk.feature.reading.data.model.DiscussionDataResponse
import com.tokopedia.talk.feature.reading.presentation.uimodel.SortOption
import com.tokopedia.talk.feature.reading.presentation.adapter.uimodel.TalkReadingHeaderModel
import com.tokopedia.talk.feature.reading.presentation.adapter.uimodel.TalkReadingUiModel
import com.tokopedia.talk.feature.reading.presentation.widget.OnCategorySelectedListener
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.list.ListItemUnify

object TalkReadingMapper {

    const val SORT_CATEGORY = "Urutkan"
    const val SORT_POPULAR = "Terpopuler"
    const val SORT_LATEST = "Terbaru"

    fun mapDiscussionAggregateResponseToTalkReadingHeaderModel(discussionAggregate: DiscussionAggregate,
                                                               showBottomSheet: () -> Unit,
                                                               onCategorySelectedListener: OnCategorySelectedListener): TalkReadingHeaderModel {
        return TalkReadingHeaderModel(
                discussionAggregate.productName,
                discussionAggregate.thumbnail,
                discussionAggregate.category.mapToSortFilter(showBottomSheet, onCategorySelectedListener)
        )
    }

    fun mapDiscussionDataResponseToTalkReadingUiModel(discussionDataResponse: DiscussionDataResponse): List<TalkReadingUiModel> {
        val result = mutableListOf<TalkReadingUiModel>()
        discussionDataResponse.question.forEach {
            result.add(TalkReadingUiModel(it))
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
            is SortOption.SortByLike -> SORT_POPULAR
            else -> SORT_LATEST
        }
    }

    fun mapSelectedSortToString(selectedSortOption: SortOption?): String {
        return selectedSortOption?.id?.name?.toLowerCase() ?: ""
    }

    private fun List<DiscussionAggregateCategory>.mapToSortFilter(showBottomSheet: () -> Unit,
                                                                  onCategorySelectedListener: OnCategorySelectedListener): ArrayList<SortFilterItem> {
        val result = arrayListOf<SortFilterItem>()
        val stringBuilder = StringBuilder()
        val sortChip = SortFilterItem(title = SORT_CATEGORY, listener = showBottomSheet)
        result.add(sortChip)
        this.forEach {
            val sortFilterItem = SortFilterItem(stringBuilder.append(it.text).append(" (").append(it.counter).append(")").toString())
            sortFilterItem.setChipListener(onCategorySelectedListener)
            result.add(sortFilterItem)
            stringBuilder.clear()
        }
        return result
    }

    private fun SortFilterItem.setChipListener(onCategorySelectedListener: OnCategorySelectedListener) {
        this.listener = {
            this.toggle()
            onCategorySelectedListener.onCategorySelected(this.title.toString(), this.type)
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
package com.tokopedia.talk.feature.reading.data.mapper

import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.talk.feature.reading.data.model.DiscussionAggregate
import com.tokopedia.talk.feature.reading.data.model.DiscussionAggregateCategory
import com.tokopedia.talk.feature.reading.data.model.SortOption
import com.tokopedia.talk.feature.reading.presentation.adapter.uimodel.TalkReadingHeaderModel
import com.tokopedia.unifycomponents.list.ListItemUnify

object TalkReadingMapper {

    const val SORT_CATEGORY = "Urutkan"
    const val SORT_POPULAR = "Terpopuler"
    const val SORT_LATEST = "Terbaru"

    fun mapDiscussionAggregateResponseToTalkReadingHeaderModel(discussionAggregate: DiscussionAggregate, showBottomSheet: () -> Unit): TalkReadingHeaderModel {
        return TalkReadingHeaderModel(
                discussionAggregate.productName,
                discussionAggregate.thumbnail,
                discussionAggregate.category.mapToSortFilter(showBottomSheet)
        )
    }

    fun mapDiscussionDataResponseToTalkReadingUiModel() {

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

    private fun List<DiscussionAggregateCategory>.mapToSortFilter(showBottomSheet: () -> Unit): ArrayList<SortFilterItem> {
        val result = arrayListOf<SortFilterItem>()
        val stringBuilder = StringBuilder()
        result.add(SortFilterItem(title = SORT_CATEGORY, listener = showBottomSheet))
        this.forEach {
            val sortFilterItem = SortFilterItem(stringBuilder.append(it.text).append(" (").append(it.counter).append(")").toString())
            result.add(sortFilterItem)
            stringBuilder.clear()
        }
        return result
    }


}
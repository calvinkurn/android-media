package com.tokopedia.talk.feature.reading.data.mapper

import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.talk.feature.reading.data.model.DiscussionAggregate
import com.tokopedia.talk.feature.reading.data.model.DiscussionAggregateCategory
import com.tokopedia.talk.feature.reading.presentation.adapter.uimodel.TalkReadingHeaderModel
import com.tokopedia.unifycomponents.ChipsUnify

object TalkReadingMapper {

    const val SORT_CATEGORY = "Urutkan"

    fun mapDiscussionAggregateResponseToTalkReadingHeaderModel(discussionAggregate: DiscussionAggregate, showBottomSheet: () -> Unit): TalkReadingHeaderModel {
        return TalkReadingHeaderModel(
                discussionAggregate.productName,
                discussionAggregate.thumbnail,
                discussionAggregate.category.mapToSortFilter(showBottomSheet)
        )
    }

    fun mapDiscussionDataResponseToTalkReadingUiModel() {

    }

    private fun List<DiscussionAggregateCategory>.mapToSortFilter(showBottomSheet: () -> Unit): List<SortFilterItem> {
        val result = mutableListOf<SortFilterItem>()
        val stringBuilder = StringBuilder()
        this.forEach {
            val sortFilterItem = if(it.text != SORT_CATEGORY) {
                SortFilterItem(stringBuilder.append(it.text).append(" (").append(it.counter).append(")").toString())
            } else {
                SortFilterItem(it.text, showBottomSheet)
            }
            result.add(sortFilterItem)
        }
        return result
    }


}
package com.tokopedia.reviewseller.feature.reviewdetail.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.reviewseller.R
import com.tokopedia.reviewseller.feature.reviewdetail.view.model.TopicUiModel
import com.tokopedia.sortfilter.SortFilter
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifyprinciples.Typography

class TopicViewHolder(val view: View) : AbstractViewHolder<TopicUiModel>(view) {

    companion object {
        @JvmStatic
        val LAYOUT = R.layout.item_topic_review_detail
        private const val DEFAULT_RATING_BAR_VALUE = 0
    }

    private val sortFilterTopics: SortFilter = view.findViewById(R.id.topicSortFilterTopic)
    private val labelResultFeedback: Typography = view.findViewById(R.id.labelResultFeedback)

    override fun bind(element: TopicUiModel) {
//        sortFilterTopics.addItem(element.sortFilterItemList)
        sortFilterTopics.addItem(dataStaticItemSortFilter())
        labelResultFeedback.text = String.format(getString(R.string.count_review_label), element.countFeedback)
    }

    private fun dataStaticItemSortFilter(): ArrayList<SortFilterItem> {
        val itemSortFilterList = ArrayList<SortFilterItem>()

        itemSortFilterList.apply {
            add(SortFilterItem(
                    title = "Kategori (99)",
                    type = ChipsUnify.TYPE_NORMAL,
                    size = ChipsUnify.SIZE_SMALL)
            )
            add(SortFilterItem(
                    title = "Kemasan (105)",
                    type = ChipsUnify.TYPE_NORMAL,
                    size = ChipsUnify.SIZE_SMALL)
            )
            add(SortFilterItem(
                    title = "Harga (900)",
                    type = ChipsUnify.TYPE_NORMAL,
                    size = ChipsUnify.SIZE_SMALL)
            )
            add(SortFilterItem(
                    title = "Shipping (205)",
                    type = ChipsUnify.TYPE_NORMAL,
                    size = ChipsUnify.SIZE_SMALL)
            )
        }
        return itemSortFilterList
    }
}
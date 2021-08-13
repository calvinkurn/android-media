package com.tokopedia.review.feature.reviewdetail.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.review.R
import com.tokopedia.review.common.util.toggle
import com.tokopedia.review.feature.reviewdetail.view.adapter.SellerReviewDetailAdapter.Companion.PAYLOAD_TOPIC_FILTER_REVIEW_COUNT
import com.tokopedia.review.feature.reviewdetail.view.adapter.SellerReviewDetailListener
import com.tokopedia.review.feature.reviewdetail.view.model.SortFilterItemWrapper
import com.tokopedia.review.feature.reviewdetail.view.model.TopicUiModel
import com.tokopedia.sortfilter.SortFilter
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifyprinciples.Typography

class TopicViewHolder(val view: View, private val fragmentListener: SellerReviewDetailListener) : AbstractViewHolder<TopicUiModel>(view) {

    companion object {
        @JvmStatic
        val LAYOUT = R.layout.item_topic_review_detail
        private const val ITEM_SORT_FILTER_EMPTY = 0
    }

    private val sortFilterTopics: SortFilter = view.findViewById(R.id.topicSortFilterTopic)
    private val resultFeedbackLabel: Typography = view.findViewById(R.id.resultFeedbackLabel)

    override fun bind(element: TopicUiModel) {
        sortFilterTopics.apply {
            sortFilterItems.removeAllViews()
            indicatorCounter = 0
            addItem(dataItemSortFilter(element.sortFilterItemList))
            parentListener = {
                fragmentListener.onParentTopicFilterClicked()
            }
        }
        setReviewCount(element.countFeedback.orZero())
    }

    override fun bind(element: TopicUiModel?, payloads: MutableList<Any>) {
        super.bind(element, payloads)
        if (element == null || payloads.isEmpty()) {
            return
        }

        when (payloads[0] as Int) {
            PAYLOAD_TOPIC_FILTER_REVIEW_COUNT -> setReviewCount(element.countFeedback.orZero())
        }
    }

    private fun setReviewCount(countFeedBack: Long) {
        resultFeedbackLabel.text = MethodChecker.fromHtml(view.context.getString(R.string.count_review_label, countFeedBack))
    }

    private fun dataItemSortFilter(sortFilterItemList: ArrayList<SortFilterItemWrapper>): ArrayList<SortFilterItem> {
        val itemSortFilterList = ArrayList<SortFilterItem>()

        itemSortFilterList.addAll(sortFilterItemList.map {
            SortFilterItem(title = it.sortFilterItem?.title
                    ?: "", type = sortFilterMapper(it.isSelected, it.count), size = ChipsUnify.SIZE_SMALL)
        })

        itemSortFilterList.forEach {
            it.listener = {
                if (it.type != ChipsUnify.TYPE_DISABLE) {
                    it.toggle()
                    fragmentListener.onChildTopicFilterClicked(it, adapterPosition)
                }
            }
        }

        return itemSortFilterList
    }

    private fun sortFilterMapper(isSelected: Boolean, count: Int): String {
        return when {
            isSelected -> {
                ChipsUnify.TYPE_SELECTED
            }
            count == ITEM_SORT_FILTER_EMPTY -> {
                ChipsUnify.TYPE_DISABLE
            }
            else -> {
                ChipsUnify.TYPE_NORMAL
            }
        }
    }

}
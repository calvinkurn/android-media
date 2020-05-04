package com.tokopedia.reviewseller.feature.reviewdetail.view.adapter.viewholder

import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.reviewseller.R
import com.tokopedia.reviewseller.common.util.toggle
import com.tokopedia.reviewseller.feature.reviewdetail.view.adapter.SellerReviewDetailAdapter.Companion.PAYLOAD_TOPIC_FILTER_REVIEW_COUNT
import com.tokopedia.reviewseller.feature.reviewdetail.view.adapter.SellerReviewDetailListener
import com.tokopedia.reviewseller.feature.reviewdetail.view.model.SortFilterItemWrapper
import com.tokopedia.reviewseller.feature.reviewdetail.view.model.TopicUiModel
import com.tokopedia.sortfilter.SortFilter
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifyprinciples.Typography

class TopicViewHolder(val view: View, private val fragmentListener: SellerReviewDetailListener) : AbstractViewHolder<TopicUiModel>(view) {

    companion object {
        @JvmStatic
        val LAYOUT = R.layout.item_topic_review_detail
    }

    private val sortFilterTopics: SortFilter = view.findViewById(R.id.topicSortFilterTopic)
    private val chipsSortFilter: ChipsUnify = view.findViewById(R.id.chipsSortFilter)
    private val resultFeedbackLabel: Typography = view.findViewById(R.id.resultFeedbackLabel)

    private var countSortFilter = 0

    override fun bind(element: TopicUiModel) {

        countSortFilter = element.sortFilterItemList.count {
            it.isSelected
        }

        if(element.sortFilterItemList.size.isZero()) {
            chipsSortFilter.apply {
                chip_text.text = getString(R.string.sort_label)
                chipImageResource = ContextCompat.getDrawable(itemView.context, R.drawable.ic_filter_icon)
                setOnClickListener {
                    toggle()
                    fragmentListener.onSortTopicClicked(itemView)
                }
                show()
            }
        } else {
            sortFilterTopics.apply {
                sortFilterItems.removeAllViews()
                addItem(dataItemSortFilter(element.sortFilterItemList))
                indicatorCounter = countSortFilter
                parentListener = {
                    fragmentListener.onParentTopicFilterClicked()
                }
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

    private fun setReviewCount(countFeedBack: Int) {
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
            count == 0 -> {
                ChipsUnify.TYPE_DISABLE
            }
            else -> {
                ChipsUnify.TYPE_NORMAL
            }
        }
    }

}
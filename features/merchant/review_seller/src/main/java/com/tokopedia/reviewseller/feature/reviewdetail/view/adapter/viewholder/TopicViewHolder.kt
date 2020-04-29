package com.tokopedia.reviewseller.feature.reviewdetail.view.adapter.viewholder

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.reviewseller.R
import com.tokopedia.reviewseller.common.util.toggle
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
    private val resultFeedbackLabel: Typography = view.findViewById(R.id.resultFeedbackLabel)

    override fun bind(element: TopicUiModel) {
        sortFilterTopics.apply {
            sortFilterItems.removeAllViews()
            addItem(dataStaticItemSortFilter(element.sortFilterItemList))
            parentListener = {
                fragmentListener.onParentTopicFilterClicked()
            }
        }

        resultFeedbackLabel.text = setReviewCountBold(element.countFeedback.orZero())
    }

    private fun setReviewCountBold(reviewCount: Int): SpannableString {
        val strView = getString(R.string.count_review_label).substring(0, 11)
        val strFormat = String.format(getString(R.string.count_review_label), reviewCount.toString())
        val strReviewSpan = SpannableString(strFormat)
        val strLengthView = strView.length + 1
        strReviewSpan.setSpan(StyleSpan(Typeface.BOLD), strLengthView, strLengthView + reviewCount.toString().length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return strReviewSpan
    }

    private fun dataStaticItemSortFilter(sortFilterItemList: ArrayList<SortFilterItemWrapper>): ArrayList<SortFilterItem> {
        val itemSortFilterList = ArrayList<SortFilterItem>()

        itemSortFilterList.addAll(sortFilterItemList.map { SortFilterItem(title = it.sortFilterItem?.title ?: "", type = sortFilterMapper(it.isSelected, it.count), size = ChipsUnify.SIZE_SMALL) })

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
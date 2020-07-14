package com.tokopedia.reviewseller.feature.inboxreview.presentation.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.reviewseller.R
import com.tokopedia.reviewseller.feature.inboxreview.presentation.adapter.FilterInboxReviewListener
import com.tokopedia.reviewseller.feature.inboxreview.presentation.model.FilterInboxReviewUiModel

class FilterInboxReviewViewHolder(view: View,
                                  private val filterInboxReviewListener: FilterInboxReviewListener):
        AbstractViewHolder<FilterInboxReviewUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_filter_inbox_review
    }

    override fun bind(element: FilterInboxReviewUiModel) {
        with(itemView) {
            element.sortFilterInboxItemList.mapIndexed { index, sortFilterInboxItemWrapper ->
                if(index == 0) {

                } else {

                }
            }
        }
    }
}
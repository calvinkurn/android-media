package com.tokopedia.reviewseller.feature.inboxreview.presentation.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.reviewseller.R
import com.tokopedia.reviewseller.feature.inboxreview.presentation.adapter.FeedbackInboxReviewListener
import com.tokopedia.reviewseller.feature.inboxreview.presentation.model.FeedbackInboxUiModel

class InboxReviewFeedbackViewHolder(view: View,
                                    private val feedbackInboxReviewListener: FeedbackInboxReviewListener): AbstractViewHolder<FeedbackInboxUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_empty_state_list_rating_product
    }

    override fun bind(element: FeedbackInboxUiModel) {
        with(itemView) {

        }
    }

}
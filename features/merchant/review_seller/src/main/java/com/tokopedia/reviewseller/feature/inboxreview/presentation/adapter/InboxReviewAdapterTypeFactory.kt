package com.tokopedia.reviewseller.feature.inboxreview.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.reviewseller.feature.inboxreview.presentation.model.FeedbackInboxUiModel
import com.tokopedia.reviewseller.feature.inboxreview.presentation.model.InboxReviewEmptyUiModel
import com.tokopedia.reviewseller.feature.inboxreview.presentation.model.InboxReviewErrorUiModel
import com.tokopedia.reviewseller.feature.inboxreview.presentation.viewholder.*

class InboxReviewAdapterTypeFactory(
        private val feedbackInboxReviewListener: FeedbackInboxReviewListener,
        private val globalErrorStateListener: GlobalErrorStateListener
) : BaseAdapterTypeFactory(), InboxReviewTypeFactory {

    override fun type(inboxReviewEmptyUiModel: InboxReviewEmptyUiModel): Int {
        return InboxReviewEmptyViewHolder.LAYOUT
    }

    override fun type(inboxReviewErrorUiModel: InboxReviewErrorUiModel): Int {
        return InboxReviewErrorViewHolder.LAYOUT
    }

    override fun type(feedbackInboxUiModel: FeedbackInboxUiModel): Int {
        return InboxReviewFeedbackViewHolder.LAYOUT
    }

    override fun type(viewModel: LoadingModel): Int {
        return InboxReviewShimmerLoadingViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when(type) {
            InboxReviewEmptyViewHolder.LAYOUT -> InboxReviewEmptyViewHolder(parent)
            InboxReviewErrorViewHolder.LAYOUT -> InboxReviewErrorViewHolder(parent, globalErrorStateListener)
            InboxReviewShimmerLoadingViewHolder.LAYOUT -> InboxReviewShimmerLoadingViewHolder(parent)
            InboxReviewFeedbackViewHolder.LAYOUT -> InboxReviewFeedbackViewHolder(parent, feedbackInboxReviewListener)
            else -> super.createViewHolder(parent, type)
        }
    }
}
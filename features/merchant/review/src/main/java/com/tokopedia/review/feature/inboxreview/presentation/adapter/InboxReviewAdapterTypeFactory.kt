package com.tokopedia.review.feature.inboxreview.presentation.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.review.feature.inboxreview.presentation.model.FeedbackInboxUiModel
import com.tokopedia.review.feature.inboxreview.presentation.model.InboxReviewEmptyUiModel
import com.tokopedia.review.feature.inboxreview.presentation.model.InboxReviewErrorUiModel
import com.tokopedia.review.feature.inboxreview.presentation.viewholder.InboxReviewEmptyViewHolder
import com.tokopedia.review.feature.inboxreview.presentation.viewholder.InboxReviewErrorViewHolder
import com.tokopedia.review.feature.inboxreview.presentation.viewholder.InboxReviewFeedbackViewHolder
import com.tokopedia.review.feature.inboxreview.presentation.viewholder.InboxReviewShimmerLoadingViewHolder
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.adapter.typefactory.ReviewMediaThumbnailTypeFactory

class InboxReviewAdapterTypeFactory(
        private val feedbackInboxReviewListener: FeedbackInboxReviewListener,
        private val globalErrorStateListener: GlobalErrorStateListener,
        private val reviewMediaThumbnailListener: ReviewMediaThumbnailTypeFactory.Listener,
        private val reviewMediaThumbnailRecycledViewPool: RecyclerView.RecycledViewPool
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
            InboxReviewFeedbackViewHolder.LAYOUT -> InboxReviewFeedbackViewHolder(parent, reviewMediaThumbnailRecycledViewPool, reviewMediaThumbnailListener, feedbackInboxReviewListener)
            else -> super.createViewHolder(parent, type)
        }
    }
}
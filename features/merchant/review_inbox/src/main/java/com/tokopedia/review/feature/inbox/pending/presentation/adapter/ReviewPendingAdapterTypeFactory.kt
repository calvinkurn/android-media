package com.tokopedia.review.feature.inbox.pending.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.review.feature.inbox.pending.presentation.adapter.uimodel.ReviewPendingCredibilityUiModel
import com.tokopedia.review.feature.inbox.pending.presentation.adapter.uimodel.ReviewPendingEmptyUiModel
import com.tokopedia.review.feature.inbox.pending.presentation.adapter.uimodel.ReviewPendingOvoIncentiveUiModel
import com.tokopedia.review.feature.inbox.pending.presentation.adapter.uimodel.ReviewPendingUiModel
import com.tokopedia.review.feature.inbox.pending.presentation.adapter.viewholder.*
import com.tokopedia.review.feature.inbox.pending.presentation.util.ReviewPendingItemListener

class ReviewPendingAdapterTypeFactory(private val reviewPendingItemListener: ReviewPendingItemListener) : ReviewPendingTypeFactory, BaseAdapterTypeFactory() {

    override fun type(reviewPendingUiModel: ReviewPendingUiModel): Int {
        return ReviewPendingViewHolder.LAYOUT
    }

    override fun type(reviewPendingOvoIncentiveUiModel: ReviewPendingOvoIncentiveUiModel): Int {
        return ReviewPendingOvoIncentiveViewHolder.LAYOUT
    }

    override fun type(loadingMoreModel: LoadingMoreModel): Int {
        return ReviewPendingLoadingViewHolder.LAYOUT
    }

    override fun type(reviewPendingCredibilityUiModel: ReviewPendingCredibilityUiModel): Int {
        return ReviewPendingCredibilityViewHolder.LAYOUT
    }

    override fun type(reviewPendingEmptyUiModel: ReviewPendingEmptyUiModel): Int {
        return ReviewPendingEmptyViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            ReviewPendingViewHolder.LAYOUT -> ReviewPendingViewHolder(parent, reviewPendingItemListener)
            ReviewPendingLoadingViewHolder.LAYOUT -> ReviewPendingLoadingViewHolder(parent)
            ReviewPendingOvoIncentiveViewHolder.LAYOUT -> ReviewPendingOvoIncentiveViewHolder(parent, reviewPendingItemListener)
            ReviewPendingCredibilityViewHolder.LAYOUT -> ReviewPendingCredibilityViewHolder(parent, reviewPendingItemListener)
            ReviewPendingEmptyViewHolder.LAYOUT -> ReviewPendingEmptyViewHolder(parent)
            else -> return super.createViewHolder(parent, type)
        }
    }
}
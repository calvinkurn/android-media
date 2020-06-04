package com.tokopedia.review.feature.inbox.pending.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.review.feature.inbox.pending.presentation.adapter.uimodel.ReviewPendingEmptyUiModel
import com.tokopedia.review.feature.inbox.pending.presentation.adapter.uimodel.ReviewPendingUiModel
import com.tokopedia.review.feature.inbox.pending.presentation.adapter.viewholder.ReviewPendingEmptyViewHolder
import com.tokopedia.review.feature.inbox.pending.presentation.adapter.viewholder.ReviewPendingViewHolder
import com.tokopedia.review.feature.inbox.pending.presentation.util.ReviewPendingItemListener

class ReviewPendingAdapterTypeFactory(private val reviewPendingItemListener: ReviewPendingItemListener) : ReviewPendingTypeFactory, BaseAdapterTypeFactory() {

    override fun type(reviewPendingUiModel: ReviewPendingUiModel): Int {
        return ReviewPendingViewHolder.LAYOUT
    }

    override fun type(reviewPendingEmptyUiModel: ReviewPendingEmptyUiModel): Int {
        return ReviewPendingEmptyViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            ReviewPendingViewHolder.LAYOUT -> ReviewPendingViewHolder(parent, reviewPendingItemListener)
            ReviewPendingEmptyViewHolder.LAYOUT -> ReviewPendingEmptyViewHolder(parent)
            else -> return super.createViewHolder(parent, type)
        }
    }
}
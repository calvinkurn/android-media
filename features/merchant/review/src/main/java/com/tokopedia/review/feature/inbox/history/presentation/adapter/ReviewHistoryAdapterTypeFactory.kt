package com.tokopedia.review.feature.inbox.history.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.review.feature.inbox.history.presentation.adapter.uimodel.ReviewHistoryUiModel
import com.tokopedia.review.feature.inbox.history.presentation.adapter.viewholder.ReviewHistoryLoadingViewHolder
import com.tokopedia.review.feature.inbox.history.presentation.adapter.viewholder.ReviewHistoryViewHolder
import com.tokopedia.review.feature.inbox.pending.presentation.adapter.viewholder.ReviewPendingLoadingViewHolder
import com.tokopedia.review.feature.inbox.pending.presentation.adapter.viewholder.ReviewPendingViewHolder

class ReviewHistoryAdapterTypeFactory : ReviewHistoryTypeFactory, BaseAdapterTypeFactory() {

    override fun type(reviewHistoryUiModel: ReviewHistoryUiModel): Int {
        return ReviewHistoryViewHolder.LAYOUT
    }

    override fun type(loadingMoreModel: LoadingMoreModel): Int {
        return ReviewHistoryLoadingViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            ReviewPendingViewHolder.LAYOUT -> ReviewHistoryViewHolder(parent)
            ReviewPendingLoadingViewHolder.LAYOUT -> ReviewHistoryLoadingViewHolder(parent)
            else -> return super.createViewHolder(parent, type)
        }
    }

}
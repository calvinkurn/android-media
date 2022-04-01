package com.tokopedia.review.feature.inbox.history.presentation.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.review.feature.inbox.history.presentation.adapter.uimodel.ReviewHistoryUiModel
import com.tokopedia.review.feature.inbox.history.presentation.adapter.viewholder.ReviewHistoryLoadingViewHolder
import com.tokopedia.review.feature.inbox.history.presentation.adapter.viewholder.ReviewHistoryViewHolder
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.adapter.typefactory.ReviewMediaThumbnailTypeFactory

class ReviewHistoryAdapterTypeFactory(
    private val reviewMediaThumbnailListener: ReviewMediaThumbnailTypeFactory.Listener,
    private val reviewMediaThumbnailRecycledViewPool: RecyclerView.RecycledViewPool
) : ReviewHistoryTypeFactory, BaseAdapterTypeFactory() {

    override fun type(reviewHistoryUiModel: ReviewHistoryUiModel): Int {
        return ReviewHistoryViewHolder.LAYOUT
    }

    override fun type(loadingMoreModel: LoadingMoreModel): Int {
        return ReviewHistoryLoadingViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            ReviewHistoryViewHolder.LAYOUT -> ReviewHistoryViewHolder(
                parent,
                reviewMediaThumbnailRecycledViewPool,
                reviewMediaThumbnailListener
            )
            ReviewHistoryLoadingViewHolder.LAYOUT -> ReviewHistoryLoadingViewHolder(parent)
            else -> return super.createViewHolder(parent, type)
        }
    }

}
package com.tokopedia.review.feature.reading.presentation.adapter


import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.review.feature.inbox.history.presentation.adapter.viewholder.ReviewHistoryViewHolder
import com.tokopedia.review.feature.reading.presentation.adapter.uimodel.ReadReviewUiModel
import com.tokopedia.review.feature.reading.presentation.adapter.viewholder.ReadReviewLoadingViewHolder
import com.tokopedia.review.feature.reading.presentation.adapter.viewholder.ReadReviewProductViewHolder

class ReadReviewAdapterTypeFactory(private val readReviewItemListener: ReadReviewItemListener) : ReadReviewTypeFactory, BaseAdapterTypeFactory() {

    override fun type(loadingMoreModel: LoadingMoreModel): Int {
        return ReadReviewLoadingViewHolder.LAYOUT
    }

    override fun type(readReviewUiModel: ReadReviewUiModel): Int {
        return ReadReviewProductViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            ReviewHistoryViewHolder.LAYOUT -> ReadReviewProductViewHolder(parent, readReviewItemListener)
            ReadReviewLoadingViewHolder.LAYOUT -> ReadReviewLoadingViewHolder(parent)
            else -> return super.createViewHolder(parent, type)
        }
    }

}
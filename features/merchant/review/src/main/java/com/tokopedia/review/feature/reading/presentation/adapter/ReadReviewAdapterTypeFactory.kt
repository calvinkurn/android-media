package com.tokopedia.review.feature.reading.presentation.adapter


import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.reviewcommon.feature.reviewer.presentation.listener.ReviewBasicInfoListener
import com.tokopedia.review.feature.reading.presentation.adapter.uimodel.ReadReviewUiModel
import com.tokopedia.review.feature.reading.presentation.adapter.viewholder.ReadReviewLoadingViewHolder
import com.tokopedia.review.feature.reading.presentation.adapter.viewholder.ReadReviewViewHolder
import com.tokopedia.review.feature.reading.presentation.listener.ReadReviewAttachedImagesListener
import com.tokopedia.review.feature.reading.presentation.listener.ReadReviewItemListener
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.adapter.typefactory.ReviewMediaThumbnailTypeFactory

class ReadReviewAdapterTypeFactory(
    private val readReviewItemListener: ReadReviewItemListener,
    private val attachedImagesClickListener: ReadReviewAttachedImagesListener,
    private val reviewBasicInfoListener: ReviewBasicInfoListener,
    private val reviewMediaThumbnailListener: ReviewMediaThumbnailTypeFactory.Listener,
    private val reviewMediaThumbnailRecycledViewPool: RecyclerView.RecycledViewPool
) : ReadReviewTypeFactory, BaseAdapterTypeFactory() {

    override fun type(loadingMoreModel: LoadingMoreModel): Int {
        return ReadReviewLoadingViewHolder.LAYOUT
    }

    override fun type(readReviewUiModel: ReadReviewUiModel): Int {
        return ReadReviewViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            ReadReviewViewHolder.LAYOUT -> ReadReviewViewHolder(
                parent,
                reviewMediaThumbnailRecycledViewPool,
                reviewMediaThumbnailListener,
                readReviewItemListener,
                reviewBasicInfoListener
            )
            ReadReviewLoadingViewHolder.LAYOUT -> ReadReviewLoadingViewHolder(parent)
            else -> return super.createViewHolder(parent, type)
        }
    }

}
package com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.adapter.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.adapter.viewholder.ReviewMediaImageThumbnailViewHolder
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.adapter.viewholder.ReviewMediaVideoThumbnailViewHolder
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uimodel.ReviewMediaImageThumbnailUiModel
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uimodel.ReviewMediaThumbnailVisitable
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uimodel.ReviewMediaVideoThumbnailUiModel

class ReviewMediaThumbnailTypeFactory(
    private val listener: Listener
) : BaseAdapterTypeFactory() {

    @Suppress("UNUSED_PARAMETER")
    fun type(reviewMediaImageThumbnailUiModel: ReviewMediaImageThumbnailUiModel): Int {
        return ReviewMediaImageThumbnailViewHolder.LAYOUT
    }

    @Suppress("UNUSED_PARAMETER")
    fun type(reviewMediaVideoThumbnailUiModel: ReviewMediaVideoThumbnailUiModel): Int {
        return ReviewMediaVideoThumbnailViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            ReviewMediaImageThumbnailViewHolder.LAYOUT -> ReviewMediaImageThumbnailViewHolder(parent, listener)
            ReviewMediaVideoThumbnailViewHolder.LAYOUT -> ReviewMediaVideoThumbnailViewHolder(parent, listener)
            else -> super.createViewHolder(parent, type)
        }
    }

    interface Listener {
        fun onMediaItemClicked(item: ReviewMediaThumbnailVisitable, position: Int)
        fun onRemoveMediaItemClicked(item: ReviewMediaThumbnailVisitable, position: Int)
    }
}
package com.tokopedia.review.feature.gallery.presentation.adapter


import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.review.feature.gallery.presentation.adapter.uimodel.ReviewGalleryImageThumbnailUiModel
import com.tokopedia.review.feature.gallery.presentation.adapter.uimodel.ReviewGalleryVideoThumbnailUiModel
import com.tokopedia.review.feature.gallery.presentation.adapter.viewholder.ReviewGalleryImageThumbnailViewHolder
import com.tokopedia.review.feature.gallery.presentation.adapter.viewholder.ReviewGalleryLoadingViewHolder
import com.tokopedia.review.feature.gallery.presentation.adapter.viewholder.ReviewGalleryVideoThumbnailViewHolder

class ReviewGalleryAdapterTypeFactory : ReviewGalleryTypeFactory, BaseAdapterTypeFactory() {

    override fun type(reviewGalleryImageThumbnailUiModel: ReviewGalleryImageThumbnailUiModel): Int {
        return ReviewGalleryImageThumbnailViewHolder.LAYOUT
    }

    override fun type(reviewGalleryVideoThumbnailUiModel: ReviewGalleryVideoThumbnailUiModel): Int {
        return ReviewGalleryVideoThumbnailViewHolder.LAYOUT
    }

    override fun type(viewModel: LoadingMoreModel?): Int {
        return ReviewGalleryLoadingViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            ReviewGalleryImageThumbnailViewHolder.LAYOUT -> ReviewGalleryImageThumbnailViewHolder(parent)
            ReviewGalleryVideoThumbnailViewHolder.LAYOUT -> ReviewGalleryVideoThumbnailViewHolder(parent)
            ReviewGalleryLoadingViewHolder.LAYOUT -> ReviewGalleryLoadingViewHolder(parent)
            else -> return super.createViewHolder(parent, type)
        }
    }

}
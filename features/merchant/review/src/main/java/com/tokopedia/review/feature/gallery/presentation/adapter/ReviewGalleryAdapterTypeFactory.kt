package com.tokopedia.review.feature.gallery.presentation.adapter


import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.review.feature.gallery.presentation.adapter.uimodel.ReviewGalleryUiModel
import com.tokopedia.review.feature.gallery.presentation.adapter.viewholder.ReviewGalleryLoadingViewHolder
import com.tokopedia.review.feature.gallery.presentation.adapter.viewholder.ReviewGalleryViewHolder

class ReviewGalleryAdapterTypeFactory : ReviewGalleryTypeFactory, BaseAdapterTypeFactory() {

    override fun type(reviewGalleryUiModel: ReviewGalleryUiModel): Int {
        return ReviewGalleryViewHolder.LAYOUT
    }

    override fun type(viewModel: LoadingMoreModel?): Int {
        return ReviewGalleryLoadingViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            ReviewGalleryViewHolder.LAYOUT -> ReviewGalleryViewHolder(parent)
            ReviewGalleryLoadingViewHolder.LAYOUT -> ReviewGalleryLoadingViewHolder(parent)
            else -> return super.createViewHolder(parent, type)
        }
    }

}
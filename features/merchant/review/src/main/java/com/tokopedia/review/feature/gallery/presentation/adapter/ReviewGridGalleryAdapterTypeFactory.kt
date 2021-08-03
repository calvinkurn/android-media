package com.tokopedia.review.feature.gallery.presentation.adapter


import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.review.feature.gallery.presentation.adapter.uimodel.ReviewGridGalleryUiModel
import com.tokopedia.review.feature.gallery.presentation.adapter.viewholder.ReviewGridGalleryViewHolder
import com.tokopedia.review.feature.reading.presentation.adapter.viewholder.ReadReviewViewHolder
import com.tokopedia.review.feature.reading.presentation.listener.ReadReviewAttachedImagesListener
import com.tokopedia.review.feature.reading.presentation.listener.ReadReviewItemListener

class ReviewGridGalleryAdapterTypeFactory() : ReviewGridGalleryTypeFactory, BaseAdapterTypeFactory() {

    override fun type(reviewGridGalleryUiModel: ReviewGridGalleryUiModel): Int {
        return ReviewGridGalleryViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            ReviewGridGalleryViewHolder.LAYOUT -> ReviewGridGalleryViewHolder(parent)
            else -> return super.createViewHolder(parent, type)
        }
    }

}
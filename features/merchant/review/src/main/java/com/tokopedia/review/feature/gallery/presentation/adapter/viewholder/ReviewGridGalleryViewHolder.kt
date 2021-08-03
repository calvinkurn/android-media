package com.tokopedia.review.feature.gallery.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.review.R
import com.tokopedia.review.feature.gallery.presentation.adapter.uimodel.ReviewGridGalleryUiModel

class ReviewGridGalleryViewHolder(view: View): AbstractViewHolder<ReviewGridGalleryUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_review_grid_gallery
    }

    override fun bind(element: ReviewGridGalleryUiModel) {

    }
}
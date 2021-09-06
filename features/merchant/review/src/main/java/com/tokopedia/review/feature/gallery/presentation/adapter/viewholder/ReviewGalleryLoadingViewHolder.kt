package com.tokopedia.review.feature.gallery.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.review.R

class ReviewGalleryLoadingViewHolder(view: View) : AbstractViewHolder<LoadingMoreModel>(view) {

    companion object {
        val LAYOUT = R.layout.widget_review_gallery_loading_item
    }

    override fun bind(element: LoadingMoreModel?) {
        // No Op
    }
}
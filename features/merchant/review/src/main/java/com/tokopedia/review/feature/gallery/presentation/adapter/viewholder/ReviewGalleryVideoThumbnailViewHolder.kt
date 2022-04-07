package com.tokopedia.review.feature.gallery.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.review.R
import com.tokopedia.review.databinding.ItemReviewGalleryVideoThumbnailBinding
import com.tokopedia.review.feature.gallery.presentation.adapter.uimodel.ReviewGalleryVideoThumbnailUiModel
import com.tokopedia.review.feature.gallery.presentation.listener.ReviewGalleryMediaThumbnailListener

class ReviewGalleryVideoThumbnailViewHolder(
    view: View,
    reviewGalleryMediaThumbnailListener: ReviewGalleryMediaThumbnailListener
) : AbstractViewHolder<ReviewGalleryVideoThumbnailUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_review_gallery_video_thumbnail
    }

    private val binding = ItemReviewGalleryVideoThumbnailBinding.bind(view)

    init {
        binding.root.setListener(reviewGalleryMediaThumbnailListener)
    }

    override fun bind(element: ReviewGalleryVideoThumbnailUiModel) {
        binding.root.bind(element)
    }
}

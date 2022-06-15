package com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.reviewcommon.R
import com.tokopedia.reviewcommon.databinding.ItemReviewMediaImageThumbnailBinding
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.adapter.typefactory.ReviewMediaThumbnailTypeFactory
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uimodel.ReviewMediaImageThumbnailUiModel
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.widget.ReviewMediaImageThumbnail

class ReviewMediaImageThumbnailViewHolder(
    view: View,
    private val listener: ReviewMediaThumbnailTypeFactory.Listener?
): AbstractViewHolder<ReviewMediaImageThumbnailUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_review_media_image_thumbnail
    }

    private val binding = ItemReviewMediaImageThumbnailBinding.bind(view)
    private val reviewMediaImageThumbnailListener = ReviewMediaImageThumbnailListener()
    private var element: ReviewMediaImageThumbnailUiModel? = null

    init {
        binding.root.setListener(reviewMediaImageThumbnailListener)
    }

    override fun bind(element: ReviewMediaImageThumbnailUiModel) {
        this.element = element
        binding.root.updateUi(element.uiState)
    }

    override fun bind(element: ReviewMediaImageThumbnailUiModel, payloads: MutableList<Any>) {
        bind(element)
    }

    private inner class ReviewMediaImageThumbnailListener: ReviewMediaImageThumbnail.Listener {
        override fun onMediaItemClicked() {
            element?.let {
                listener?.onMediaItemClicked(it, adapterPosition)
            }
        }
    }
}

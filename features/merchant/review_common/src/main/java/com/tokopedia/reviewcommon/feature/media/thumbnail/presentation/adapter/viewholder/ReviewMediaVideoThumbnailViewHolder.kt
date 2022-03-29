package com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.reviewcommon.R
import com.tokopedia.reviewcommon.databinding.ItemReviewMediaVideoThumbnailBinding
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.adapter.typefactory.ReviewMediaThumbnailTypeFactory
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uimodel.ReviewMediaVideoThumbnailUiModel
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.widget.ReviewMediaVideoThumbnail

class ReviewMediaVideoThumbnailViewHolder(
    view: View,
    private val listener: ReviewMediaThumbnailTypeFactory.Listener?
): AbstractViewHolder<ReviewMediaVideoThumbnailUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_review_media_video_thumbnail
    }

    private val binding = ItemReviewMediaVideoThumbnailBinding.bind(view)
    private val reviewMediaVideoThumbnailListener = ReviewMediaVideoThumbnailListener()
    private var element: ReviewMediaVideoThumbnailUiModel? = null

    init {
        binding.root.setListener(reviewMediaVideoThumbnailListener)
    }

    override fun bind(element: ReviewMediaVideoThumbnailUiModel) {
        this.element = element
        binding.root.updateUi(element.uiState)
    }

    private inner class ReviewMediaVideoThumbnailListener: ReviewMediaVideoThumbnail.Listener {
        override fun onMediaItemClicked() {
            element?.let { listener?.onMediaItemClicked(it, adapterPosition.coerceAtLeast(Int.ZERO)) }
        }

        override fun onRemoveMediaItemClicked() {
            element?.let { listener?.onRemoveMediaItemClicked(it, adapterPosition.coerceAtLeast(Int.ZERO)) }
        }
    }
}
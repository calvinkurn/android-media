package com.tokopedia.review.feature.createreputation.presentation.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.review.R
import com.tokopedia.review.databinding.ItemCreateReviewMediaPreviewVideoBinding
import com.tokopedia.review.feature.createreputation.presentation.adapter.CreateReviewMediaAdapter
import com.tokopedia.review.feature.createreputation.presentation.uimodel.visitable.CreateReviewMediaUiModel
import com.tokopedia.review.feature.createreputation.presentation.widget.CreateReviewVideoPreviewThumbnail

class CreateReviewMediaPreviewVideoViewHolder(
    view: View,
    private val listener: CreateReviewMediaAdapter.Listener
) : AbstractViewHolder<CreateReviewMediaUiModel.Video>(view),
    CreateReviewVideoPreviewThumbnail.Listener {

    companion object {
        val LAYOUT = R.layout.item_create_review_media_preview_video
    }

    private val binding = ItemCreateReviewMediaPreviewVideoBinding.bind(view)

    init {
        binding.layoutCreateReviewVideoPreview.setListener(this)
    }

    override fun bind(element: CreateReviewMediaUiModel.Video) {
        binding.layoutCreateReviewVideoPreview.bind(element)
    }

    override fun bind(element: CreateReviewMediaUiModel.Video, payloads: MutableList<Any>) {
        binding.layoutCreateReviewVideoPreview.bind(element, payloads)
    }

    override fun onVideoPreviewThumbnailClicked() {
        listener.onAddMediaClicked(true)
    }

    override fun onRemoveMediaClicked(element: CreateReviewMediaUiModel.Video) {
        listener.onRemoveMediaClicked(element)
    }
}
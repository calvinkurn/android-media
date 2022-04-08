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
    private var element: CreateReviewMediaUiModel.Video? = null

    init {
        binding.layoutCreateReviewVideoPreview.setListener(this)
    }

    override fun bind(element: CreateReviewMediaUiModel.Video) {
        this.element = element
        binding.layoutCreateReviewVideoPreview.bind(element)
    }

    override fun bind(element: CreateReviewMediaUiModel.Video, payloads: MutableList<Any>) {
        this.element = element
        binding.layoutCreateReviewVideoPreview.bind(payloads)
    }

    override fun onVideoPreviewThumbnailClicked() {
        listener.onAddMediaClicked()
    }

    override fun onRemoveMediaClicked() {
        element?.let { listener.onRemoveMediaClicked(it) }
    }
}
package com.tokopedia.review.feature.createreputation.presentation.viewholder.old

import android.view.View
import com.tokopedia.review.R
import com.tokopedia.review.databinding.ItemVideoChooserReviewBinding
import com.tokopedia.review.feature.createreputation.presentation.uimodel.visitable.CreateReviewMediaUiModel
import com.tokopedia.review.feature.createreputation.presentation.widget.CreateReviewVideoPreviewThumbnail

class VideoReviewViewHolder(
    view: View,
    private val listener: Listener
) : BaseImageReviewViewHolder<CreateReviewMediaUiModel.Video>(view),
    CreateReviewVideoPreviewThumbnail.Listener {

    companion object {
        val LAYOUT = R.layout.item_video_chooser_review
    }

    private val binding = ItemVideoChooserReviewBinding.bind(view)
    private var element: CreateReviewMediaUiModel.Video? = null

    init {
        binding.layoutCreateReviewVideoPreview.setListener(this)
    }

    override fun bind(element: CreateReviewMediaUiModel.Video) {
        this.element = element
        binding.layoutCreateReviewVideoPreview.bind(element)
    }

    override fun onVideoPreviewThumbnailClicked() {
        listener.onAddMediaClicked()
    }

    override fun onRemoveMediaClicked() {
        element?.let { listener.onRemoveVideoClicked(it) }
    }

    interface Listener {
        fun onAddMediaClicked()
        fun onRemoveVideoClicked(video: CreateReviewMediaUiModel.Video)
    }
}
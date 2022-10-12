package com.tokopedia.review.feature.createreputation.presentation.viewholder

import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateInterpolator
import androidx.transition.Fade
import androidx.transition.TransitionManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.review.R
import com.tokopedia.review.databinding.ItemCreateReviewMediaPreviewImageBinding
import com.tokopedia.review.feature.createreputation.presentation.adapter.CreateReviewMediaAdapter
import com.tokopedia.review.feature.createreputation.presentation.uimodel.visitable.CreateReviewMediaUiModel

class CreateReviewMediaPreviewImageViewHolder(
    view: View,
    private val listener: CreateReviewMediaAdapter.Listener
) : AbstractViewHolder<CreateReviewMediaUiModel.Image>(view) {

    companion object {
        private const val TRANSITION_DURATION = 300L
        val LAYOUT = R.layout.item_create_review_media_preview_image
    }

    private val binding = ItemCreateReviewMediaPreviewImageBinding.bind(view)
    private val transitionHandler = TransitionHandler()

    init {
        attachAddMoreClickListener()
    }

    override fun bind(element: CreateReviewMediaUiModel.Image) {
        with(binding) {
            setupImageThumbnail(element.uri)
            setupImageState(element.state)
            setupRemoveIconClickListener(element)
        }
    }

    override fun bind(element: CreateReviewMediaUiModel.Image, payloads: MutableList<Any>) {
        payloads.firstOrNull().let {
            if (it is Bundle) {
                applyImageStateChange(it)
            }
        }
        binding.setupRemoveIconClickListener(element)
    }

    private fun ItemCreateReviewMediaPreviewImageBinding.setupImageThumbnail(uri: String) {
        ivCreateReviewImagePreviewThumbnail.urlSrc = uri
    }

    private fun ItemCreateReviewMediaPreviewImageBinding.setupRemoveIconClickListener(element: CreateReviewMediaUiModel.Image) {
        icCreateReviewImageRemove.setOnClickListener {
            listener.onRemoveMediaClicked(element)
        }
    }

    private fun setupImageState(state: CreateReviewMediaUiModel.State) {
        when (state) {
            CreateReviewMediaUiModel.State.UPLOADING -> transitionHandler.transitionToUploading()
            CreateReviewMediaUiModel.State.UPLOAD_FAILED -> transitionHandler.transitionToUploadFailed()
            else -> transitionHandler.transitionToThumbnailOnly()
        }
    }

    private fun applyImageStateChange(payload: Bundle) {
        payload.get(CreateReviewMediaUiModel.PAYLOAD_MEDIA_STATE).let {
            if (it is CreateReviewMediaUiModel.State) {
                setupImageState(it)
            }
        }
    }

    private fun attachAddMoreClickListener() {
        binding.root.setOnClickListener { listener.onAddMediaClicked(true) }
    }

    private inner class TransitionHandler {
        private val fadeTransition by lazy(LazyThreadSafetyMode.NONE) {
            Fade().apply {
                duration = TRANSITION_DURATION
                addTarget(binding.viewCreateReviewImagePreviewOverlayUploading)
                addTarget(binding.loaderCreateReviewImagePreviewUploadProgress)
                addTarget(binding.viewCreateReviewImagePreviewOverlayFailed)
                addTarget(binding.icCreateReviewImagePreviewUploadFailed)
                interpolator = AccelerateInterpolator()
            }
        }

        private fun ItemCreateReviewMediaPreviewImageBinding.showUploading() {
            groupCreateReviewImagePreviewUploadFailed.gone()
            groupCreateReviewImagePreviewUploading.show()
        }

        private fun ItemCreateReviewMediaPreviewImageBinding.showUploadFailed() {
            groupCreateReviewImagePreviewUploading.gone()
            groupCreateReviewImagePreviewUploadFailed.show()
        }

        private fun ItemCreateReviewMediaPreviewImageBinding.showThumbnailOnly() {
            groupCreateReviewImagePreviewUploading.gone()
            groupCreateReviewImagePreviewUploadFailed.gone()
        }

        fun transitionToUploading() {
            TransitionManager.beginDelayedTransition(binding.root, fadeTransition)
            binding.showUploading()
        }

        fun transitionToUploadFailed() {
            TransitionManager.beginDelayedTransition(binding.root, fadeTransition)
            binding.showUploadFailed()
        }

        fun transitionToThumbnailOnly() {
            TransitionManager.beginDelayedTransition(binding.root, fadeTransition)
            binding.showThumbnailOnly()
        }
    }
}
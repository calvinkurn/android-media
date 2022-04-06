package com.tokopedia.review.feature.createreputation.presentation.viewholder

import android.media.MediaMetadataRetriever
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateInterpolator
import androidx.transition.Fade
import androidx.transition.TransitionManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.review.R
import com.tokopedia.review.databinding.ItemCreateReviewMediaPreviewVideoBinding
import com.tokopedia.review.feature.createreputation.presentation.adapter.CreateReviewMediaAdapter
import com.tokopedia.review.feature.createreputation.presentation.uimodel.visitable.CreateReviewMediaUiModel
import com.tokopedia.reviewcommon.extension.isMoreThanZero
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import kotlin.coroutines.CoroutineContext

class CreateReviewMediaPreviewVideoViewHolder(
    view: View,
    private val listener: CreateReviewMediaAdapter.Listener
) : AbstractViewHolder<CreateReviewMediaUiModel.Video>(view), CoroutineScope {

    companion object {
        private const val TRANSITION_DURATION = 300L

        val LAYOUT = R.layout.item_create_review_media_preview_video
    }

    private val binding = ItemCreateReviewMediaPreviewVideoBinding.bind(view)
    private val transitionHandler = TransitionHandler()
    private val supervisorJob = SupervisorJob()
    private var element: CreateReviewMediaUiModel.Video? = null

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + supervisorJob

    init {
        binding.root.setOnClickListener {
            listener.onAddMediaClicked()
        }
        binding.icCreateReviewVideoRemove.setOnClickListener {
            element?.let { listener.onRemoveMediaClicked(it) }
        }
    }

    override fun bind(element: CreateReviewMediaUiModel.Video) {
        supervisorJob.cancelChildren()
        this.element = element
        with(binding) {
            setupVideoThumbnail(element.uri)
            setupVideoDuration(element.uri)
            setupVideoState(element.state)
        }
    }

    override fun bind(element: CreateReviewMediaUiModel.Video, payloads: MutableList<Any>) {
        this.element = element
        payloads.firstOrNull().let {
            if (it is Bundle) {
                applyVideoStateChange(it)
            }
        }
    }

    private fun ItemCreateReviewMediaPreviewVideoBinding.setupVideoThumbnail(uri: String) {
        ivCreateReviewVideoPreviewThumbnail.urlSrc = uri
    }

    private fun ItemCreateReviewMediaPreviewVideoBinding.setupVideoDuration(uri: String) {
        launchCatchError(context = Dispatchers.IO, block = {
            val retriever = MediaMetadataRetriever()
            retriever.setDataSource(uri)
            ensureActive()
            val durationString = retriever.extractMetadata(
                MediaMetadataRetriever.METADATA_KEY_DURATION
            )
            val durationMillis = durationString.toLongOrZero()
            val minute = TimeUnit.MILLISECONDS.toMinutes(durationMillis)
            val second = TimeUnit.MILLISECONDS.toSeconds(
                durationMillis - TimeUnit.MINUTES.toMillis(minute)
            )
            ensureActive()
            withContext(Dispatchers.Main) {
                if (minute.isMoreThanZero() && second.isMoreThanZero()) {
                    tvCreateReviewVideoPreviewDuration.run {
                        text = buildString {
                            append(if (minute < 10) "0" else "")
                            append(minute)
                            append(":")
                            append(if (second < 10) "0" else "")
                        }
                        show()
                    }
                } else {
                    tvCreateReviewVideoPreviewDuration.gone()
                }
            }
        }, onError = {
            withContext(Dispatchers.Main) {
                tvCreateReviewVideoPreviewDuration.gone()
            }
        })
    }

    private fun setupVideoState(state: CreateReviewMediaUiModel.State) {
        when (state) {
            CreateReviewMediaUiModel.State.UPLOADING -> transitionHandler.transitionToUploading()
            CreateReviewMediaUiModel.State.UPLOAD_FAILED -> transitionHandler.transitionToUploadFailed()
            else -> transitionHandler.transitionToThumbnailOnly()
        }
    }

    private fun applyVideoStateChange(payload: Bundle) {
        payload.get(CreateReviewMediaUiModel.PAYLOAD_MEDIA_STATE).let {
            if (it is CreateReviewMediaUiModel.State) {
                setupVideoState(it)
            }
        }
    }

    private inner class TransitionHandler {
        private val fadeTransition by lazy(LazyThreadSafetyMode.NONE) {
            Fade().apply {
                duration = TRANSITION_DURATION
                addTarget(binding.viewCreateReviewVideoPreviewOverlayUploading)
                addTarget(binding.loaderCreateReviewVideoPreviewUploadProgress)
                addTarget(binding.viewCreateReviewVideoPreviewOverlayFailed)
                addTarget(binding.icCreateReviewVideoPreviewUploadFailed)
                interpolator = AccelerateInterpolator()
            }
        }

        private fun ItemCreateReviewMediaPreviewVideoBinding.showUploading() {
            groupCreateReviewVideoPreviewUploadFailed.gone()
            groupCreateReviewVideoPreviewUploading.show()
        }

        private fun ItemCreateReviewMediaPreviewVideoBinding.showUploadFailed() {
            groupCreateReviewVideoPreviewUploading.gone()
            groupCreateReviewVideoPreviewUploadFailed.show()
        }

        private fun ItemCreateReviewMediaPreviewVideoBinding.showThumbnailOnly() {
            groupCreateReviewVideoPreviewUploading.gone()
            groupCreateReviewVideoPreviewUploadFailed.gone()
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
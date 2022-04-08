package com.tokopedia.review.feature.createreputation.presentation.widget

import android.content.Context
import android.media.MediaMetadataRetriever
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.AccelerateInterpolator
import androidx.transition.Fade
import androidx.transition.TransitionManager
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.review.databinding.WidgetCreateReviewMediaPreviewVideoThumbnailBinding
import com.tokopedia.review.feature.createreputation.presentation.uimodel.visitable.CreateReviewMediaUiModel
import com.tokopedia.reviewcommon.extension.isMoreThanZero
import com.tokopedia.reviewcommon.feature.media.player.video.presentation.widget.ReviewVideoPlayer
import com.tokopedia.reviewcommon.feature.media.player.video.presentation.widget.ReviewVideoPlayerListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import kotlin.coroutines.CoroutineContext

class CreateReviewVideoPreviewThumbnail @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = Int.ZERO
) : BaseCreateReviewCustomView<WidgetCreateReviewMediaPreviewVideoThumbnailBinding>(
    context,
    attrs,
    defStyleAttr
), CoroutineScope, ReviewVideoPlayerListener {

    companion object {
        private const val TRANSITION_DURATION = 300L
    }

    override val binding = WidgetCreateReviewMediaPreviewVideoThumbnailBinding.inflate(
        /* inflater = */ LayoutInflater.from(context),
        /* parent = */ this,
        /* attachToParent = */ true
    )

    private val transitionHandler = TransitionHandler()
    private val supervisorJob = SupervisorJob()
    private val reviewVideoPlayer = ReviewVideoPlayer(
        context = context,
        minBufferDuration = 50,
        maxBufferDuration = 50,
        minPlaybackStartBuffer = 50,
        minPlaybackResumeBuffer = 50
    )
    private var listener: Listener? = null

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + supervisorJob

    init {
        binding.root.setOnClickListener {
            listener?.onVideoPreviewThumbnailClicked()
        }
        binding.icCreateReviewVideoRemove.setOnClickListener {
            listener?.onRemoveMediaClicked()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        reviewVideoPlayer.cleanupVideoPlayer()
    }

    override fun onReviewVideoPlayerIsPlaying() {
        // noop
    }

    override fun onReviewVideoPlayerIsBuffering() {
        // noop
    }

    override fun onReviewVideoPlayerIsPaused() {
        binding.loaderCreateReviewVideoPreviewThumbnail.gone()
    }

    override fun onReviewVideoPlayerIsPreloading() {
        // noop
    }

    override fun onReviewVideoPlayerIsEnded() {
        // noop
    }

    override fun onReviewVideoPlayerError() {
        // noop
    }

    fun bind(element: CreateReviewMediaUiModel.Video) {
        supervisorJob.cancelChildren()
        with(binding) {
            setupVideoThumbnail(element.uri)
            setupVideoDuration(element.uri)
            setupVideoState(element.state)
        }
    }

    fun bind(payloads: MutableList<Any>) {
        payloads.firstOrNull().let {
            if (it is Bundle) {
                applyVideoStateChange(it)
            }
        }
    }

    fun setListener(newListener: Listener) {
        listener = newListener
    }

    private fun WidgetCreateReviewMediaPreviewVideoThumbnailBinding.setupVideoThumbnail(uri: String) {
        binding.loaderCreateReviewVideoPreviewThumbnail.show()
        binding.loaderCreateReviewVideoPreviewDuration.show()
        reviewVideoPlayer.initializeVideoPlayer(
            uri,
            playerViewCreateReviewVideoPreviewThumbnail,
            this@CreateReviewVideoPreviewThumbnail,
            true
        )
    }

    private fun WidgetCreateReviewMediaPreviewVideoThumbnailBinding.setupVideoDuration(uri: String) {
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
                loaderCreateReviewVideoPreviewDuration.gone()
            }
        }, onError = {
            withContext(Dispatchers.Main) {
                tvCreateReviewVideoPreviewDuration.gone()
                loaderCreateReviewVideoPreviewDuration.gone()
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

        private fun WidgetCreateReviewMediaPreviewVideoThumbnailBinding.showUploading() {
            groupCreateReviewVideoPreviewUploadFailed.gone()
            groupCreateReviewVideoPreviewUploading.show()
        }

        private fun WidgetCreateReviewMediaPreviewVideoThumbnailBinding.showUploadFailed() {
            groupCreateReviewVideoPreviewUploading.gone()
            groupCreateReviewVideoPreviewUploadFailed.show()
        }

        private fun WidgetCreateReviewMediaPreviewVideoThumbnailBinding.showThumbnailOnly() {
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

    interface Listener {
        fun onVideoPreviewThumbnailClicked()
        fun onRemoveMediaClicked()
    }
}
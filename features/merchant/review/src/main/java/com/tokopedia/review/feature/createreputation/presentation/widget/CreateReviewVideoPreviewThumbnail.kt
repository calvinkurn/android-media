package com.tokopedia.review.feature.createreputation.presentation.widget

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.AccelerateInterpolator
import androidx.transition.Fade
import androidx.transition.TransitionManager
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.review.databinding.WidgetCreateReviewMediaPreviewVideoThumbnailBinding
import com.tokopedia.review.feature.createreputation.presentation.uimodel.visitable.CreateReviewMediaUiModel
import com.tokopedia.reviewcommon.extension.isMoreThanZero
import com.tokopedia.reviewcommon.feature.media.player.video.presentation.widget.ReviewVideoPlayer
import com.tokopedia.reviewcommon.feature.media.player.video.presentation.widget.ReviewVideoPlayerListener
import java.util.concurrent.TimeUnit

class CreateReviewVideoPreviewThumbnail @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = Int.ZERO
) : BaseReviewCustomView<WidgetCreateReviewMediaPreviewVideoThumbnailBinding>(
    context,
    attrs,
    defStyleAttr
), ReviewVideoPlayerListener {

    companion object {
        private const val MIN_NON_LEADING_ZERO_DURATION = 10
    }

    override val binding = WidgetCreateReviewMediaPreviewVideoThumbnailBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    private val transitionHandler = TransitionHandler()
    private val reviewVideoPlayer = ReviewVideoPlayer(
        context = context,
        minBufferDuration = 50,
        maxBufferDuration = 50,
        minPlaybackStartBuffer = 50,
        minPlaybackResumeBuffer = 50
    )
    private var listener: Listener? = null

    init {
        binding.root.setOnClickListener {
            listener?.onVideoPreviewThumbnailClicked()
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
        binding.setupVideoDuration(reviewVideoPlayer.getVideoDurationSecond())
    }

    override fun onReviewVideoPlayerIsPreloading() {
        // noop
    }

    override fun onReviewVideoPlayerIsEnded() {
        // noop
    }

    override fun onReviewVideoPlayerError(errorCode: String) {
        binding.tvCreateReviewVideoPreviewDuration.gone()
        binding.loaderCreateReviewVideoPreviewDuration.gone()
    }

    fun bind(element: CreateReviewMediaUiModel.Video) {
        with(binding) {
            setupVideoThumbnail(element.uri)
            setupVideoState(element.state)
            setupRemoveIconClickListener(element)
        }
    }

    fun bind(element: CreateReviewMediaUiModel.Video, payloads: MutableList<Any>) {
        payloads.firstOrNull().let {
            if (it is Bundle) {
                applyVideoStateChange(it)
            }
        }
        binding.setupRemoveIconClickListener(element)
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

    private fun WidgetCreateReviewMediaPreviewVideoThumbnailBinding.setupVideoDuration(
        videoDurationSecond: Long
    ) {
        val minute = TimeUnit.SECONDS.toMinutes(videoDurationSecond)
        val second = videoDurationSecond - TimeUnit.MINUTES.toSeconds(minute)
        if (minute.isMoreThanZero() || second.isMoreThanZero()) {
            tvCreateReviewVideoPreviewDuration.run {
                text = buildString {
                    append(if (minute < MIN_NON_LEADING_ZERO_DURATION) "0" else "")
                    append(minute)
                    append(":")
                    append(if (second < MIN_NON_LEADING_ZERO_DURATION) "0" else "")
                    append(second)
                }
                show()
            }
        } else {
            tvCreateReviewVideoPreviewDuration.gone()
        }
        loaderCreateReviewVideoPreviewDuration.gone()
    }

    private fun WidgetCreateReviewMediaPreviewVideoThumbnailBinding.setupRemoveIconClickListener(element: CreateReviewMediaUiModel.Video) {
        icCreateReviewVideoRemove.setOnClickListener {
            listener?.onRemoveMediaClicked(element)
        }
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
                duration = ANIMATION_DURATION
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
        fun onRemoveMediaClicked(element: CreateReviewMediaUiModel.Video)
    }
}
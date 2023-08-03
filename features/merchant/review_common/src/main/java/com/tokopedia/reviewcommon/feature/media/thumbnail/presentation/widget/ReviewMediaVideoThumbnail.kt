package com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.clearImage
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.utils.RemoteConfig
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.reviewcommon.R
import com.tokopedia.reviewcommon.databinding.WidgetReviewMediaVideoThumbnailBinding
import com.tokopedia.reviewcommon.feature.media.player.video.presentation.widget.ReviewVideoPlayer
import com.tokopedia.reviewcommon.feature.media.player.video.presentation.widget.ReviewVideoPlayerListener
import com.tokopedia.reviewcommon.feature.media.thumbnail.presentation.uistate.ReviewMediaVideoThumbnailUiState
import com.tokopedia.unifycomponents.BaseCustomView

class ReviewMediaVideoThumbnail @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = Int.ZERO
) : BaseCustomView(context, attrs, defStyleAttr), ReviewVideoPlayerListener {

    private val binding = WidgetReviewMediaVideoThumbnailBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )
    private val listener: ViewListeners = ViewListeners()
    private val reviewVideoPlayer by lazy {
        ReviewVideoPlayer(
            context = context,
            minBufferDuration = 50,
            maxBufferDuration = 50,
            minPlaybackStartBuffer = 50,
            minPlaybackResumeBuffer = 50
        )
    }
    private var uiState: ReviewMediaVideoThumbnailUiState? = null

    init {
        binding.icReviewMediaVideoThumbnailPlayButton.loadImage(R.drawable.ic_review_media_video_thumbnail_play)
        binding.playerViewOverlayClickable.setOnClickListener(listener)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        uiState?.let { updateUi(it) }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        if (RemoteConfig.glideM3U8ThumbnailLoaderEnabled(context)) {
            binding.ivReviewMediaVideoThumbnail.clearImage()
        } else {
            reviewVideoPlayer.cleanupVideoPlayer()
        }
    }

    override fun onReviewVideoPlayerIsPlaying() {
        // noop
    }

    override fun onReviewVideoPlayerIsBuffering() {
        // noop
    }

    override fun onReviewVideoPlayerIsPaused() {
        binding.reviewMediaVideoThumbnailBrokenOverlay.gone()
        binding.icReviewMediaVideoThumbnailBroken.gone()
        binding.loaderReviewMediaVideoThumbnail.gone()
    }

    override fun onReviewVideoPlayerIsPreloading() {
        // noop
    }

    override fun onReviewVideoPlayerIsEnded() {
        // noop
    }

    override fun onReviewVideoPlayerError(errorCode: String) {
        binding.loaderReviewMediaVideoThumbnail.gone()
        when (uiState) {
            is ReviewMediaVideoThumbnailUiState.Showing -> {
                binding.reviewMediaVideoThumbnailBrokenOverlay.show()
                binding.icReviewMediaVideoThumbnailBroken.show()
            }
            else -> {
                binding.reviewMediaVideoThumbnailBrokenOverlay.show()
                binding.icReviewMediaVideoThumbnailBroken.gone()
            }
        }
    }

    private fun WidgetReviewMediaVideoThumbnailBinding.setupVideoThumbnail(
        uiState: ReviewMediaVideoThumbnailUiState
    ) {
        if (RemoteConfig.glideM3U8ThumbnailLoaderEnabled(context)) {
            setupVideoThumbnailWithGlide(uiState)
            playerViewReviewMediaVideoThumbnail.gone()
            ivReviewMediaVideoThumbnail.show()
        } else {
            setupVideoThumbnailWithExoPlayer(uiState)
            playerViewReviewMediaVideoThumbnail.show()
            ivReviewMediaVideoThumbnail.gone()
        }
    }

    private fun WidgetReviewMediaVideoThumbnailBinding.setupVideoThumbnailWithGlide(
        uiState: ReviewMediaVideoThumbnailUiState
    ) {
        binding.loaderReviewMediaVideoThumbnail.show()
        binding.ivReviewMediaVideoThumbnail.loadImage(uiState.url) {
            listener(onSuccess = { _, _ ->
                onReviewVideoPlayerIsPaused()
            }, onError = { throwable ->
                    val errorCode = throwable?.let {
                        ErrorHandler.getErrorMessagePair(context, it, ErrorHandler.Builder()).second
                    }.orEmpty()
                    onReviewVideoPlayerError(errorCode)
                })
        }
        icReviewMediaVideoThumbnailPlayButton.showWithCondition(
            uiState is ReviewMediaVideoThumbnailUiState.Showing && uiState.playable
        )
    }

    private fun WidgetReviewMediaVideoThumbnailBinding.setupVideoThumbnailWithExoPlayer(
        uiState: ReviewMediaVideoThumbnailUiState
    ) {
        binding.loaderReviewMediaVideoThumbnail.show()
        reviewVideoPlayer.initializeVideoPlayer(
            uiState.url,
            binding.playerViewReviewMediaVideoThumbnail,
            this@ReviewMediaVideoThumbnail,
            true
        )
        icReviewMediaVideoThumbnailPlayButton.showWithCondition(
            uiState is ReviewMediaVideoThumbnailUiState.Showing && uiState.playable
        )
    }

    private fun WidgetReviewMediaVideoThumbnailBinding.showVideoThumbnail(
        uiState: ReviewMediaVideoThumbnailUiState.Showing
    ) {
        groupReviewMediaVideoThumbnailSeeMore.gone()
        reviewMediaVideoThumbnailBrokenOverlay.gone()
        icReviewMediaVideoThumbnailBroken.gone()
        setupVideoThumbnail(uiState)
    }

    private fun WidgetReviewMediaVideoThumbnailBinding.showVideoThumbnailSeeMore(
        uiState: ReviewMediaVideoThumbnailUiState.ShowingSeeMore
    ) {
        reviewMediaVideoThumbnailBrokenOverlay.gone()
        icReviewMediaVideoThumbnailBroken.gone()
        groupReviewMediaVideoThumbnailSeeMore.show()
        tvReviewMediaVideoThumbnailSeeMore.text = uiState.totalMediaCountFmt
        setupVideoThumbnail(uiState)
    }

    fun updateUi(uiState: ReviewMediaVideoThumbnailUiState) {
        this.uiState = uiState
        when (uiState) {
            is ReviewMediaVideoThumbnailUiState.Showing -> {
                binding.showVideoThumbnail(uiState)
            }
            is ReviewMediaVideoThumbnailUiState.ShowingSeeMore -> {
                binding.showVideoThumbnailSeeMore(uiState)
            }
        }
    }

    fun setListener(newListener: Listener) {
        listener.listener = newListener
    }

    private inner class ViewListeners : OnClickListener {
        var listener: Listener? = null
        override fun onClick(v: View?) {
            when (v) {
                binding.playerViewOverlayClickable -> {
                    listener?.onMediaItemClicked()
                }
            }
        }
    }

    interface Listener {
        fun onMediaItemClicked()
    }
}

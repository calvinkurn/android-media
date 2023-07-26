package com.tokopedia.review.feature.gallery.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.clearImage
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.utils.RemoteConfig
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.review.R
import com.tokopedia.review.common.util.getReviewStar
import com.tokopedia.review.databinding.WidgetReviewGalleryVideoThumbnailBinding
import com.tokopedia.review.feature.gallery.presentation.adapter.uimodel.ReviewGalleryVideoThumbnailUiModel
import com.tokopedia.review.feature.gallery.presentation.listener.ReviewGalleryMediaThumbnailListener
import com.tokopedia.reviewcommon.feature.media.player.video.presentation.widget.ReviewVideoPlayer
import com.tokopedia.reviewcommon.feature.media.player.video.presentation.widget.ReviewVideoPlayerListener
import com.tokopedia.unifycomponents.BaseCustomView

class ReviewGalleryVideoThumbnail @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = Int.ZERO
) : BaseCustomView(context, attrs, defStyleAttr), ReviewVideoPlayerListener {

    private val binding = WidgetReviewGalleryVideoThumbnailBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )
    private val reviewVideoPlayer by lazy {
        ReviewVideoPlayer(
            context = binding.root.context,
            minBufferDuration = 50,
            maxBufferDuration = 50,
            minPlaybackStartBuffer = 50,
            minPlaybackResumeBuffer = 50
        )
    }
    private var element: ReviewGalleryVideoThumbnailUiModel? = null
    private var reviewGalleryMediaThumbnailListener: ReviewGalleryMediaThumbnailListener? = null

    init {
        setupLayout()
        binding.root.setOnClickListener {
            element?.let { reviewGalleryMediaThumbnailListener?.onThumbnailClicked(it) }
        }
    }

    fun bind(element: ReviewGalleryVideoThumbnailUiModel) {
        this.element = element
        with(binding) {
            setupBrokenOverlay()
            setupPlayButton()
            setupThumbnail(element.mediaUrl)
            setupRating(element.rating)
            setupVariant(element.variantName)
        }
    }

    fun setListener(newListener: ReviewGalleryMediaThumbnailListener) {
        reviewGalleryMediaThumbnailListener = newListener
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        element?.let { element -> bind(element) }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        if (RemoteConfig.glideM3U8ThumbnailLoaderEnabled(context)) {
            binding.ivReviewGalleryVideoThumbnail.clearImage()
        } else {
            reviewVideoPlayer.cleanupVideoPlayer()
        }
    }

    private fun setupLayout() {
        binding.ivReviewGalleryVideoThumbnailPlayButton.loadImage(com.tokopedia.reviewcommon.R.drawable.ic_review_media_video_thumbnail_play)
    }

    private fun WidgetReviewGalleryVideoThumbnailBinding.setupBrokenOverlay() {
        reviewMediaGalleryVideoThumbnailBrokenOverlay.hide()
        icReviewGalleryVideoThumbnailBroken.hide()
    }

    private fun WidgetReviewGalleryVideoThumbnailBinding.setupPlayButton() {
        ivReviewGalleryVideoThumbnailPlayButton.hide()
    }

    private fun WidgetReviewGalleryVideoThumbnailBinding.setupThumbnail(videoUrl: String) {
        if (RemoteConfig.glideM3U8ThumbnailLoaderEnabled(context)) {
            setupThumbnailWithGlide(videoUrl)
            playerViewReviewGalleryVideoThumbnail.gone()
            ivReviewGalleryVideoThumbnail.show()
        } else {
            setupThumbnailWithExoPlayer(videoUrl)
            ivReviewGalleryVideoThumbnail.gone()
            playerViewReviewGalleryVideoThumbnail.show()
        }
    }

    private fun WidgetReviewGalleryVideoThumbnailBinding.setupThumbnailWithGlide(videoUrl: String) {
        loaderReviewGalleryVideoThumbnail.show()
        ivReviewGalleryVideoThumbnail.loadImage(videoUrl) {
            listener(onSuccess = { _, _ ->
                onReviewVideoPlayerIsPaused()
            }, onError = { throwable ->
                val errorCode = throwable?.let {
                    ErrorHandler.getErrorMessagePair(context, it, ErrorHandler.Builder()).second
                }.orEmpty()
                onReviewVideoPlayerError(errorCode)
            })
        }
    }

    private fun WidgetReviewGalleryVideoThumbnailBinding.setupThumbnailWithExoPlayer(videoUrl: String) {
        loaderReviewGalleryVideoThumbnail.show()
        reviewVideoPlayer.initializeVideoPlayer(
            videoUrl,
            playerViewReviewGalleryVideoThumbnail,
            this@ReviewGalleryVideoThumbnail,
            true
        )
    }

    private fun WidgetReviewGalleryVideoThumbnailBinding.setupRating(rating: Int) {
        ivReviewGalleryVideoThumbnailRating.loadImage(getReviewStar(rating))
    }

    private fun WidgetReviewGalleryVideoThumbnailBinding.setupVariant(variantName: String) {
        tvReviewGalleryVideoThumbnailProductVariantName.run {
            text = context.getString(R.string.review_gallery_variant, variantName)
            showWithCondition(variantName.isNotBlank())
        }
    }

    override fun onReviewVideoPlayerIsPlaying() {
        // noop
    }

    override fun onReviewVideoPlayerIsBuffering() {
        // noop
    }

    override fun onReviewVideoPlayerIsPaused() {
        with(binding) {
            ivReviewGalleryVideoThumbnailPlayButton.show()
            reviewMediaGalleryVideoThumbnailBrokenOverlay.gone()
            icReviewGalleryVideoThumbnailBroken.gone()
            loaderReviewGalleryVideoThumbnail.gone()
        }
    }

    override fun onReviewVideoPlayerIsPreloading() {
        // noop
    }

    override fun onReviewVideoPlayerIsEnded() {
        // noop
    }

    override fun onReviewVideoPlayerError(errorCode: String) {
        with(binding) {
            reviewMediaGalleryVideoThumbnailBrokenOverlay.show()
            icReviewGalleryVideoThumbnailBroken.show()
            ivReviewGalleryVideoThumbnailPlayButton.gone()
            loaderReviewGalleryVideoThumbnail.gone()
        }
    }
}

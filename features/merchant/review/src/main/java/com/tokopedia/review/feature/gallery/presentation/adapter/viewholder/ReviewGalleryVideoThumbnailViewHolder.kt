package com.tokopedia.review.feature.gallery.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadImage
import com.tokopedia.review.R
import com.tokopedia.review.common.util.getReviewStar
import com.tokopedia.review.databinding.ItemReviewGalleryVideoThumbnailBinding
import com.tokopedia.review.feature.gallery.presentation.adapter.uimodel.ReviewGalleryVideoThumbnailUiModel
import com.tokopedia.review.feature.gallery.presentation.listener.ReviewGalleryMediaThumbnailListener
import com.tokopedia.reviewcommon.feature.media.player.video.presentation.widget.ReviewVideoPlayer
import com.tokopedia.reviewcommon.feature.media.player.video.presentation.widget.ReviewVideoPlayerListener

class ReviewGalleryVideoThumbnailViewHolder(
    view: View,
    reviewGalleryMediaThumbnailListener: ReviewGalleryMediaThumbnailListener
) : AbstractViewHolder<ReviewGalleryVideoThumbnailUiModel>(view), ReviewVideoPlayerListener {

    companion object {
        val LAYOUT = R.layout.item_review_gallery_video_thumbnail
    }

    private val binding = ItemReviewGalleryVideoThumbnailBinding.bind(view)
    private val reviewVideoPlayer = ReviewVideoPlayer(
        context = binding.root.context,
        minBufferDuration = 50,
        maxBufferDuration = 50,
        minPlaybackStartBuffer = 50,
        minPlaybackResumeBuffer = 50
    )
    private var element: ReviewGalleryVideoThumbnailUiModel? = null

    init {
        setupLayout()
        binding.root.setOnClickListener {
            element?.let { reviewGalleryMediaThumbnailListener.onThumbnailClicked(it) }
        }
    }

    override fun bind(element: ReviewGalleryVideoThumbnailUiModel) {
        this.element = element
        with(binding) {
            setupBrokenOverlay()
            setupPlayButton()
            setupThumbnail(element.mediaUrl)
            setupRating(element.rating)
            setupVariant(element.variantName)
        }
    }

    private fun setupLayout() {
        binding.ivReviewGalleryVideoThumbnailPlayButton.loadImage(com.tokopedia.reviewcommon.R.drawable.ic_review_media_video_thumbnail_play)
    }

    private fun ItemReviewGalleryVideoThumbnailBinding.setupBrokenOverlay() {
        reviewMediaGalleryVideoThumbnailBrokenOverlay.hide()
        icReviewGalleryVideoThumbnailBroken.hide()
    }

    private fun ItemReviewGalleryVideoThumbnailBinding.setupPlayButton() {
        ivReviewGalleryVideoThumbnailPlayButton.hide()
    }

    private fun ItemReviewGalleryVideoThumbnailBinding.setupThumbnail(videoUrl: String) {
        loaderReviewGalleryVideoThumbnail.show()
        reviewVideoPlayer.initializeVideoPlayer(
            videoUrl,
            playerViewReviewGalleryVideoThumbnail,
            this@ReviewGalleryVideoThumbnailViewHolder,
            true
        )
    }

    private fun ItemReviewGalleryVideoThumbnailBinding.setupRating(rating: Int) {
        ivReviewGalleryVideoThumbnailRating.loadImage(getReviewStar(rating))
    }

    private fun ItemReviewGalleryVideoThumbnailBinding.setupVariant(variantName: String) {
        tvReviewGalleryVideoThumbnailProductVariantName.run {
            text = getString(R.string.review_gallery_variant, variantName)
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

    override fun onReviewVideoPlayerError() {
        with(binding) {
            reviewMediaGalleryVideoThumbnailBrokenOverlay.show()
            icReviewGalleryVideoThumbnailBroken.show()
            ivReviewGalleryVideoThumbnailPlayButton.gone()
            loaderReviewGalleryVideoThumbnail.gone()
        }
    }
}

package com.tokopedia.review.feature.gallery.presentation.adapter.viewholder

import android.annotation.SuppressLint
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadImage
import com.tokopedia.review.R
import com.tokopedia.review.common.util.getReviewStar
import com.tokopedia.review.databinding.ItemReviewGalleryVideoThumbnailBinding
import com.tokopedia.review.feature.gallery.presentation.adapter.uimodel.ReviewGalleryVideoThumbnailUiModel
import com.tokopedia.review.feature.gallery.presentation.listener.ReviewGalleryMediaThumbnailListener

class ReviewGalleryVideoThumbnailViewHolder(
    view: View,
    reviewGalleryMediaThumbnailListener: ReviewGalleryMediaThumbnailListener
) : AbstractViewHolder<ReviewGalleryVideoThumbnailUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_review_gallery_video_thumbnail
    }

    private val binding = ItemReviewGalleryVideoThumbnailBinding.bind(view)
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
        with(binding) {
            ivReviewGalleryVideoThumbnailPlayButton.loadImage(com.tokopedia.reviewcommon.R.drawable.ic_review_media_video_thumbnail_play)
            ivReviewGalleryVideoThumbnail.onUrlLoaded = { success ->
                ivReviewGalleryVideoThumbnailPlayButton.showWithCondition(success)
                reviewMediaGalleryVideoThumbnailBrokenOverlay.showWithCondition(!success)
                icReviewGalleryVideoThumbnailBroken.showWithCondition(!success)
            }
        }
    }

    private fun ItemReviewGalleryVideoThumbnailBinding.setupBrokenOverlay() {
        reviewMediaGalleryVideoThumbnailBrokenOverlay.hide()
        icReviewGalleryVideoThumbnailBroken.hide()
    }

    private fun ItemReviewGalleryVideoThumbnailBinding.setupPlayButton() {
        ivReviewGalleryVideoThumbnailPlayButton.hide()
    }

    private fun ItemReviewGalleryVideoThumbnailBinding.setupThumbnail(videoUrl: String) {
        ivReviewGalleryVideoThumbnail.urlSrc = videoUrl
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
}

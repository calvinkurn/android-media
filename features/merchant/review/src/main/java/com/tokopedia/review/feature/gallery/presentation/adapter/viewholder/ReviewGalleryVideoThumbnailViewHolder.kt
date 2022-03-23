package com.tokopedia.review.feature.gallery.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadImage
import com.tokopedia.review.R
import com.tokopedia.review.common.util.getReviewStar
import com.tokopedia.review.databinding.ItemReviewGalleryVideoThumbnailBinding
import com.tokopedia.review.feature.gallery.presentation.adapter.uimodel.ReviewGalleryVideoThumbnailUiModel

class ReviewGalleryVideoThumbnailViewHolder(
    view: View
): AbstractViewHolder<ReviewGalleryVideoThumbnailUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_review_gallery_video_thumbnail
    }

    private val binding = ItemReviewGalleryVideoThumbnailBinding.bind(view)

    init {
        setupLayout()
    }

    override fun bind(element: ReviewGalleryVideoThumbnailUiModel) {
        with(binding) {
            setupBrokenOverlay()
            setupPlayButton()
            setupThumbnail(element.videoUrl)
            setupRating(element.rating)
            setupVariant(element.variantName)
        }
    }

    private fun setupLayout() {
        with(binding) {
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

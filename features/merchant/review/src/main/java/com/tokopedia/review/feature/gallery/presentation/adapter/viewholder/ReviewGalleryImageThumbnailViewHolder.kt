package com.tokopedia.review.feature.gallery.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadImage
import com.tokopedia.review.R
import com.tokopedia.review.common.util.getReviewStar
import com.tokopedia.review.databinding.ItemReviewGalleryImageThumbnailBinding
import com.tokopedia.review.feature.gallery.presentation.adapter.uimodel.ReviewGalleryImageThumbnailUiModel

class ReviewGalleryImageThumbnailViewHolder(
    view: View
) : AbstractViewHolder<ReviewGalleryImageThumbnailUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_review_gallery_image_thumbnail
    }

    private val binding = ItemReviewGalleryImageThumbnailBinding.bind(view)

    init {
        setupLayout()
    }

    override fun bind(element: ReviewGalleryImageThumbnailUiModel) {
        with(binding) {
            setupBrokenOverlay()
            setupThumbnail(element.imageUrl)
            setupRating(element.rating)
            setupVariant(element.variantName)
        }
    }

    private fun setupLayout() {
        with(binding) {
            ivReviewGalleryImageThumbnail.onUrlLoaded = { success ->
                reviewMediaGalleryImageThumbnailBrokenOverlay.showWithCondition(!success)
                icReviewGalleryImageThumbnailBroken.showWithCondition(!success)
            }
        }
    }

    private fun ItemReviewGalleryImageThumbnailBinding.setupBrokenOverlay() {
        reviewMediaGalleryImageThumbnailBrokenOverlay.hide()
        icReviewGalleryImageThumbnailBroken.hide()
    }

    private fun ItemReviewGalleryImageThumbnailBinding.setupThumbnail(imageUrl: String) {
        ivReviewGalleryImageThumbnail.setImageUrl(imageUrl)
    }

    private fun ItemReviewGalleryImageThumbnailBinding.setupRating(rating: Int) {
        ivReviewGalleryImageThumbnailRating.loadImage(getReviewStar(rating))
    }

    private fun ItemReviewGalleryImageThumbnailBinding.setupVariant(variantName: String) {
        tvReviewGalleryImageThumbnailProductVariantName.run {
            text = getString(R.string.review_gallery_variant, variantName)
            showWithCondition(variantName.isNotBlank())
        }
    }
}
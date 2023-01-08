package com.tokopedia.review.feature.gallery.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.clearImage
import com.tokopedia.media.loader.loadImage
import com.tokopedia.review.R
import com.tokopedia.review.common.util.getReviewStar
import com.tokopedia.review.databinding.ItemReviewGalleryImageThumbnailBinding
import com.tokopedia.review.feature.gallery.presentation.adapter.uimodel.ReviewGalleryImageThumbnailUiModel
import com.tokopedia.review.feature.gallery.presentation.listener.ReviewGalleryMediaThumbnailListener

class ReviewGalleryImageThumbnailViewHolder(
    view: View,
    private val reviewGalleryMediaThumbnailListener: ReviewGalleryMediaThumbnailListener
) : AbstractViewHolder<ReviewGalleryImageThumbnailUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_review_gallery_image_thumbnail
    }

    private val binding = ItemReviewGalleryImageThumbnailBinding.bind(view)
    private var element: ReviewGalleryImageThumbnailUiModel? = null

    init {
        binding.root.setOnClickListener {
            element?.let { reviewGalleryMediaThumbnailListener.onThumbnailClicked(it) }
        }
    }

    override fun bind(element: ReviewGalleryImageThumbnailUiModel) {
        this.element = element
        with(binding) {
            setupBrokenOverlay()
            setupThumbnail(element.mediaUrl)
            setupRating(element.rating)
            setupVariant(element.variantName)
            setupImpressHolder(element)
        }
    }

    override fun onViewRecycled() {
        binding.ivReviewGalleryImageThumbnail.clearImage()
        super.onViewRecycled()
    }

    private fun ItemReviewGalleryImageThumbnailBinding.setupBrokenOverlay() {
        reviewMediaGalleryImageThumbnailBrokenOverlay.hide()
        icReviewGalleryImageThumbnailBroken.hide()
    }

    private fun ItemReviewGalleryImageThumbnailBinding.setupThumbnail(imageUrl: String) {
        loaderReviewGalleryImageThumbnail.show()
        ivReviewGalleryImageThumbnail.loadImage(imageUrl) {
            listener(onError = {
                loaderReviewGalleryImageThumbnail.gone()
                reviewMediaGalleryImageThumbnailBrokenOverlay.show()
                icReviewGalleryImageThumbnailBroken.show()
            }, onSuccess = { _, _ ->
                loaderReviewGalleryImageThumbnail.gone()
                reviewMediaGalleryImageThumbnailBrokenOverlay.gone()
                icReviewGalleryImageThumbnailBroken.gone()
            })
        }
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

    private fun ItemReviewGalleryImageThumbnailBinding.setupImpressHolder(
        element: ReviewGalleryImageThumbnailUiModel
    ) {
        root.addOnImpressionListener(element.impressHolder) {
            reviewGalleryMediaThumbnailListener.onImageImpressed(element)
        }
    }
}
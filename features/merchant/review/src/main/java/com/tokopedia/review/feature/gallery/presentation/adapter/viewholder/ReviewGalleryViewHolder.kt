package com.tokopedia.review.feature.gallery.presentation.adapter.viewholder

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.media.loader.loadImage
import com.tokopedia.review.R
import com.tokopedia.review.common.util.getReviewStar
import com.tokopedia.review.feature.gallery.presentation.adapter.uimodel.ReviewGalleryUiModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class ReviewGalleryViewHolder(view: View): AbstractViewHolder<ReviewGalleryUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_review_gallery
    }

    private var image: AppCompatImageView? = null
    private var rating: ImageUnify? = null
    private var variantName: Typography? = null

    init {
        bindViews()
    }

    override fun bind(element: ReviewGalleryUiModel) {
        with(element) {
            setReviewImage(imageUrl)
            setRating(rating)
            setVariantName(variantName)
        }
    }

    private fun bindViews() {
        with(itemView) {
            image = findViewById(R.id.review_gallery_image)
            rating = findViewById(R.id.review_gallery_rating)
            variantName = findViewById(R.id.review_gallery_product_variant_name)
        }
    }

    private fun setReviewImage(imageUrl: String) {
        image?.loadImage(imageUrl)
    }

    private fun setRating(rating: Int) {
        this.rating?.loadImage(getReviewStar(rating))
    }

    private fun setVariantName(variant: String) {
        variantName?.shouldShowWithAction(variant.isNotBlank()) {
            variantName?.text = getString(R.string.review_gallery_variant, variant)
        }
    }
}
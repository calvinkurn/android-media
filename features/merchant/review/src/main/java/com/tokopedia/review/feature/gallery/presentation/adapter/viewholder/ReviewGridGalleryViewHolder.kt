package com.tokopedia.review.feature.gallery.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.media.loader.loadImage
import com.tokopedia.review.R
import com.tokopedia.review.common.util.getReviewStar
import com.tokopedia.review.feature.gallery.presentation.adapter.uimodel.ReviewGridGalleryUiModel
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class ReviewGridGalleryViewHolder(view: View): AbstractViewHolder<ReviewGridGalleryUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_review_grid_gallery
    }

    private var image: ImageUnify? = null
    private var rating: ImageUnify? = null
    private var variantName: Typography? = null

    override fun bind(element: ReviewGridGalleryUiModel) {
        bindViews()
        with(element) {
            setReviewImage(imageUrl)
            setRating(rating)
            setVariantName(variantName)
        }
    }

    private fun bindViews() {
        with(itemView) {
            image = findViewById(R.id.review_grid_gallery_image)
            rating = findViewById(R.id.review_grid_rating)
            variantName = findViewById(R.id.review_grid_product_variant_name)
        }
    }

    private fun setReviewImage(imageUrl: String) {
        image?.loadImage(imageUrl)
    }

    private fun setRating(rating: Int) {
        this.rating?.loadImage(getReviewStar(rating))
    }

    private fun setVariantName(variant: String) {
        variantName?.text = variant
    }
}
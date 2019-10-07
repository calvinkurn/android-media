package com.tokopedia.product.detail.view.fragment.partialview

import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.tokopedia.gallery.viewmodel.ImageReviewItem
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.common.data.model.product.Rating
import com.tokopedia.product.detail.view.adapter.ImageReviewAdapter
import kotlinx.android.synthetic.main.partial_product_image_review.view.*

class PartialImageReviewView private constructor(private val view: View,
                                                 private val onSeeAllReviewClick: (() -> Unit)? = null,
                                                 private val onImageReviewClick:((List<ImageReviewItem>, Int) -> Unit)? = null) {
    companion object {
        fun build(_view: View, _onSeeAllReviewClick: (() -> Unit)?, _onImageReviewClick:((List<ImageReviewItem>, Int) -> Unit)?) =
                PartialImageReviewView(_view, _onSeeAllReviewClick, _onImageReviewClick)
    }

    init {
        with(view) {
            image_review_list.layoutManager = GridLayoutManager(context, 4)
        }
    }

    fun renderData(imageReviews: List<ImageReviewItem>, rating: Rating) {
        var showSeeAll = false
        if (imageReviews.isNotEmpty()) {
            showSeeAll = imageReviews.first().hasNext
        }
        view.image_review_list.adapter = ImageReviewAdapter(imageReviews.toMutableList(), showSeeAll,onImageReviewClick)
        with(view) {
            txt_see_all_partial.setOnClickListener {
                onSeeAllReviewClick?.invoke()
            }
            review_count.text = context.getString(R.string.review_counter, rating.totalRating)
            review_rating.text = context.getString(R.string.counter_pattern_string, rating.ratingScore, 5)
            if (imageReviews.isNotEmpty())
                visible()
            else
                gone()
        }
    }
}
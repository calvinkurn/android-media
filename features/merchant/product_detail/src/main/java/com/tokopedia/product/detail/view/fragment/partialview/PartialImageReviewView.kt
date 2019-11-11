package com.tokopedia.product.detail.view.fragment.partialview

import android.graphics.Rect
import androidx.recyclerview.widget.GridLayoutManager
import android.view.View
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.gallery.viewmodel.ImageReviewItem
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.ProductInfoP2General
import com.tokopedia.product.detail.data.util.OnImageReviewClick
import com.tokopedia.product.detail.data.util.OnSeeAllReviewClick
import com.tokopedia.product.detail.view.adapter.ImageReviewAdapter
import kotlinx.android.synthetic.main.partial_product_image_review.view.*
import com.tokopedia.product.detail.data.model.review.Review


class PartialImageReviewView private constructor(private val view: View,
                                                 private val onSeeAllReviewClick: OnSeeAllReviewClick? = null,
                                                 private val onImageReviewClick: OnImageReviewClick? = null,
                                                 private val onReviewClicked: (() -> Unit)? = null) {
    companion object {
        fun build(_view: View, _onSeeAllReviewClick: (() -> Unit)?, _onImageReviewClick: ((List<ImageReviewItem>, Int) -> Unit)?, _onReviewClicked: (() -> Unit)?) =
                PartialImageReviewView(_view, _onSeeAllReviewClick, _onImageReviewClick, _onReviewClicked)
    }

    init {
        with(view) {
            image_review_list.layoutManager = GridLayoutManager(context, 4)
        }
    }

    fun renderData(productInfoP2: ProductInfoP2General) {
        val imageReviews = productInfoP2.imageReviews
        val rating = productInfoP2.rating
        val reviews = productInfoP2.helpfulReviews

        val showSeeAll = if (imageReviews.isNotEmpty()) {
            imageReviews.first().hasNext
        } else {
            false
        }

        view.image_review_list.adapter = ImageReviewAdapter(imageReviews.toMutableList(), showSeeAll, onImageReviewClick, onSeeAllReviewClick)
        with(view) {
            txt_see_all_partial.setOnClickListener {
                onReviewClicked?.invoke()
            }
            review_count.text = context.getString(R.string.review_counter, rating.totalRating)
            review_rating.text = context.getString(R.string.counter_pattern_string, rating.ratingScore, 5)

            setBackgroundAndKeepPadding(this, reviews)

            if (rating.totalRating > 0) visible() else gone()

            if (imageReviews.isNotEmpty())
                image_review_list.visible()
            else
                image_review_list.gone()
        }
    }

    fun setBackgroundAndKeepPadding(view: View, reviews: List<Review>) {
        val drawablePadding = Rect()

        view.background.getPadding(drawablePadding)

        val top = view.paddingTop + drawablePadding.top
        val left = view.paddingLeft + drawablePadding.left
        val right = view.paddingRight + drawablePadding.right
        val bottom = view.paddingBottom + drawablePadding.bottom

        view.background = if (reviews.isEmpty())
            MethodChecker.getDrawable(view.context, R.drawable.bg_bottom_shadow_top_line)
        else
            MethodChecker.getDrawable(view.context, R.drawable.bg_top_line)

        view.setPadding(left, top, right, bottom)
    }
}
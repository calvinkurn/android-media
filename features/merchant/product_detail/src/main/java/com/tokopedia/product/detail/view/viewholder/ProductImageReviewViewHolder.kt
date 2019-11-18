package com.tokopedia.product.detail.view.viewholder

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.ProductInfoP2General
import com.tokopedia.product.detail.data.model.datamodel.ProductImageReviewDataModel
import com.tokopedia.product.detail.data.model.review.Review
import com.tokopedia.product.detail.view.adapter.ImageReviewAdapter
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import kotlinx.android.synthetic.main.partial_product_image_review.view.*

class ProductImageReviewViewHolder(val view: View, val listener: DynamicProductDetailListener) : AbstractViewHolder<ProductImageReviewDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.partial_product_image_review
    }

    override fun bind(element: ProductImageReviewDataModel) {
        with(view) {
            image_review_list.layoutManager = GridLayoutManager(context, 4)
        }

        element.productInfoP2General?.let {
            renderData(it)
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

        view.image_review_list.adapter = ImageReviewAdapter(imageReviews.toMutableList(), showSeeAll, listener::onImageReviewClick, listener::onSeeAllReviewClick)
        with(view) {
            txt_see_all_partial.setOnClickListener {
                listener.onReviewClick()
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

    private fun setBackgroundAndKeepPadding(view: View, reviews: List<Review>) {
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
package com.tokopedia.product.detail.view.adapter

import android.support.v4.content.ContextCompat
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.widget.GridLayoutManager
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.gallery.customview.RatingView.getRatingDrawable
import com.tokopedia.gallery.viewmodel.ImageReviewItem
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.review.Review
import kotlinx.android.synthetic.main.item_product_most_helpful_review.view.*

class MostHelpfulReviewPagerAdapter(private val reviews: List<Review>, var onReviewClicked: (() -> Unit)?): PagerAdapter() {

    companion object {
        private const val MAX_IMAGE = 3
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`

    override fun getCount(): Int = reviews.size

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        if (container is ViewPager){
            container.removeView(`object` as View)
        }
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = container.inflateLayout(R.layout.item_product_most_helpful_review)
        val review = reviews[position]
        with(view){
            ImageHandler.loadImageRounded2(container.context, image_reviewer, review.user.image)
            txt_reviewer_name.text = MethodChecker.fromHtml(review.user.fullName)
            text_review_date.text = review.reviewCreateTime
            text_review.text = MethodChecker.fromHtml(review.message)
            iv_rating_review.setImageDrawable(ContextCompat.getDrawable(container.context,
                    getRatingDrawable(review.productRating)))

            setOnClickListener { onReviewClicked?.invoke() }
            if (review.imageAttachments.isEmpty())
                rv_review_attachment.gone()
            else {
                rv_review_attachment.layoutManager = GridLayoutManager(container.context,
                        if (review.imageAttachments.size > MAX_IMAGE) MAX_IMAGE else review.imageAttachments.size)
                rv_review_attachment.adapter = ImageReviewAdapter(
                        review.imageAttachments
                                .map { ImageReviewItem(review.reviewId.toString(), review.reviewCreateTime,
                                        review.user.fullName, it.imageThumbnailUrl, it.imageUrl, review.productRating)
                                }.toMutableList())
                rv_review_attachment.visible()
            }
        }
        container.addView(view)
        return view
    }
}
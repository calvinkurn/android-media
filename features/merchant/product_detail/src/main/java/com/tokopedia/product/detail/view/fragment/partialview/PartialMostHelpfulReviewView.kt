package com.tokopedia.product.detail.view.fragment.partialview

import android.view.View
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.review.Review
import com.tokopedia.product.detail.data.util.thousandFormatted
import com.tokopedia.product.detail.view.adapter.MostHelpfulReviewPagerAdapter
import kotlinx.android.synthetic.main.partial_most_helpful_review_view.view.*

class PartialMostHelpfulReviewView private constructor(private val view: View) {
    companion object {
        fun build(_view: View) = PartialMostHelpfulReviewView(_view)
    }

    fun renderData(reviews: List<Review>, totalReview: Int){
        with(view){
            if (reviews.isEmpty()){
                gone()
            } else {
                review_viewpager.adapter = MostHelpfulReviewPagerAdapter(reviews)
                review_page_indicator.setViewPager(review_viewpager)
                review_page_indicator.notifyDataSetChanged()
                base_view_most_helpful_review.visible()
                txt_see_all_review.text = context.getString(R.string.label_see_all_review, totalReview.thousandFormatted())
                visible()
            }
        }
    }
}
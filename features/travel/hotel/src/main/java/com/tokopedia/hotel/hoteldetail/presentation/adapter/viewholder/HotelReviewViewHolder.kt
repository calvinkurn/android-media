package com.tokopedia.hotel.hoteldetail.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.common.travel.utils.TextHtmlUtils
import com.tokopedia.hotel.R
import com.tokopedia.hotel.databinding.ItemHotelReviewBinding
import com.tokopedia.hotel.hoteldetail.presentation.model.viewmodel.HotelReview
import com.tokopedia.utils.date.DateUtil
import com.tokopedia.utils.date.toDate
import com.tokopedia.utils.date.toString

/**
 * @author by jessica on 29/04/19
 */

class HotelReviewViewHolder(val binding: ItemHotelReviewBinding) : AbstractViewHolder<HotelReview>(binding.root) {

    override fun bind(review: HotelReview) {
        with(binding) {
            reviewTitle.text = TextHtmlUtils.getTextFromHtml(review.headline)
            ratingTextView.text = review.score.toString()

            goodReviewLayout.visibility = if (review.pros.isNotBlank()) View.VISIBLE else View.GONE
            goodReviewText.text = TextHtmlUtils.getTextFromHtml(review.pros)

            badReviewLayout.visibility = if (review.cons.isNotBlank()) View.VISIBLE else View.GONE
            badReviewText.text = TextHtmlUtils.getTextFromHtml(review.cons)

            reviewerName.text = review.reviewerName
            reviewerOriginAndDate.text = String.format(getString(R.string.hotel_review_country_date), review.country,
                    review.createTime.toDate(DateUtil.YYYY_MM_DD).toString(DateUtil.DEFAULT_VIEW_FORMAT))
        }
    }

    companion object {
        val LAYOUT = R.layout.item_hotel_review
    }

}
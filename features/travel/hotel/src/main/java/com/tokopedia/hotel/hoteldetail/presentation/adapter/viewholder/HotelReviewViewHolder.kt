package com.tokopedia.hotel.hoteldetail.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.common.travel.utils.TextHtmlUtils
import com.tokopedia.hotel.R
import com.tokopedia.hotel.hoteldetail.presentation.model.viewmodel.HotelReview
import com.tokopedia.utils.date.DateUtil
import com.tokopedia.utils.date.toDate
import com.tokopedia.utils.date.toString
import kotlinx.android.synthetic.main.item_hotel_review.view.*

/**
 * @author by jessica on 29/04/19
 */

class HotelReviewViewHolder(val view: View) : AbstractViewHolder<HotelReview>(view) {

    override fun bind(review: HotelReview) {
        with(itemView) {
            review_title.text = TextHtmlUtils.getTextFromHtml(review.headline)
            rating_text_view.text = review.score.toString()

            good_review_layout.visibility = if (review.pros.isNotBlank()) View.VISIBLE else View.GONE
            good_review_text.text = TextHtmlUtils.getTextFromHtml(review.pros)

            bad_review_layout.visibility = if (review.cons.isNotBlank()) View.VISIBLE else View.GONE
            bad_review_text.text = TextHtmlUtils.getTextFromHtml(review.cons)

            reviewer_name.text = review.reviewerName
            reviewer_origin_and_date.text = String.format(resources.getString(R.string.hotel_review_country_date), review.country,
                    review.createTime.toDate(DateUtil.YYYY_MM_DD).toString(DateUtil.DEFAULT_VIEW_FORMAT))
        }
    }

    companion object {
        val LAYOUT = R.layout.item_hotel_review
    }

}
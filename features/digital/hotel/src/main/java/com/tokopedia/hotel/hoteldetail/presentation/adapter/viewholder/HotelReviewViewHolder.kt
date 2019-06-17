package com.tokopedia.hotel.hoteldetail.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.common.travel.utils.TravelDateUtil
import com.tokopedia.hotel.R
import com.tokopedia.hotel.hoteldetail.presentation.model.viewmodel.HotelReview
import kotlinx.android.synthetic.main.item_hotel_review.view.*

/**
 * @author by jessica on 29/04/19
 */

class HotelReviewViewHolder(val view: View): AbstractViewHolder<HotelReview>(view) {

    override fun bind(review: HotelReview) {
        with(itemView) {
            review_title.text = review.headline
            rating_text_view.text = review.score.toString()

            good_review_layout.visibility = if (review.pros.isNotBlank()) View.VISIBLE else View.GONE
            good_review_text.text = review.pros

            bad_review_layout.visibility = if (review.cons.isNotBlank()) View.VISIBLE else View.GONE
            bad_review_text.text = review.cons

            reviewer_name.text = review.reviewerName
            reviewer_origin_and_date.text = resources.getString(R.string.hotel_review_country_date, review.country,
                    TravelDateUtil.dateToString(TravelDateUtil.DEFAULT_VIEW_FORMAT,
                            TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD, review.createTime)))
        }
    }

    companion object {
        val LAYOUT = R.layout.item_hotel_review
    }

}
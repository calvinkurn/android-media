package com.tokopedia.hotel.hoteldetail.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.common.travel.utils.TextHtmlUtils
import com.tokopedia.common.travel.utils.TravelDateUtil
import com.tokopedia.hotel.R
import com.tokopedia.hotel.hoteldetail.presentation.model.viewmodel.HotelReview
import kotlinx.android.synthetic.main.item_hotel_detail_review.view.*

/**
 * @author by furqan on 29/04/19
 */
class HotelDetailReviewViewHolder(val view: View): RecyclerView.ViewHolder(view) {

    fun bind(element: HotelReview) {
        with(itemView) {
            tv_review_title.text = TextHtmlUtils.getTextFromHtml(element.headline)
            tv_review_description.text = TextHtmlUtils.getTextFromHtml(element.pros)
            tv_review_reviewer.text = element.reviewerName
            tv_review_score.text = element.score.toString()
            tv_review_date.text = TravelDateUtil.dateToString(TravelDateUtil.DEFAULT_VIEW_FORMAT, TravelDateUtil
                    .stringToDate(TravelDateUtil.YYYY_MM_DD, element.createTime))
        }
    }

    companion object {
        val LAYOUT = R.layout.item_hotel_detail_review
    }
}
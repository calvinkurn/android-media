package com.tokopedia.hotel.hoteldetail.presentation.adapter.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import com.tokopedia.hotel.R
import kotlinx.android.synthetic.main.item_hotel_detail_review.view.*

/**
 * @author by furqan on 29/04/19
 */
class HotelDetailReviewViewHolder(val view: View): RecyclerView.ViewHolder(view) {

    fun bind(element: String) {
        with(itemView) {
            tv_review_title.text = "Sangat Baik"
            tv_review_description.text = "Kami cukup puas dengan fasilitas yang disediakan oleh hotel tersebut. Kami cukup puas dengan fasilitas yang disediakan oleh hotel tersebut."
            tv_review_reviewer.text = "Username"
            tv_review_score.text = "8,6"
            tv_review_date.text = "01 Mei 2019"
        }
    }


    companion object {
        val LAYOUT = R.layout.item_hotel_detail_review
    }
}
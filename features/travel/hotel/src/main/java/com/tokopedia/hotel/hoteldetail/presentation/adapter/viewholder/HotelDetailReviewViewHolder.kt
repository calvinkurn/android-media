package com.tokopedia.hotel.hoteldetail.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.common.travel.utils.TextHtmlUtils
import com.tokopedia.hotel.R
import com.tokopedia.hotel.databinding.ItemHotelDetailReviewBinding
import com.tokopedia.hotel.hoteldetail.presentation.model.viewmodel.HotelReview
import com.tokopedia.utils.date.DateUtil
import com.tokopedia.utils.date.toDate
import com.tokopedia.utils.date.toString

/**
 * @author by furqan on 29/04/19
 */
class HotelDetailReviewViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemHotelDetailReviewBinding.bind(view)

    fun bind(element: HotelReview) {
        with(binding) {
            tvReviewTitle.text = TextHtmlUtils.getTextFromHtml(element.headline)
            tvReviewDescription.text = TextHtmlUtils.getTextFromHtml(element.pros)
            tvReviewReviewer.text = element.reviewerName
            tvReviewScore.text = element.score.toString()
            tvReviewDate.text = element.createTime.toDate(DateUtil.YYYY_MM_DD)
                    .toString(DateUtil.DEFAULT_VIEW_FORMAT)
        }
    }

    companion object {
        val LAYOUT = R.layout.item_hotel_detail_review
    }
}

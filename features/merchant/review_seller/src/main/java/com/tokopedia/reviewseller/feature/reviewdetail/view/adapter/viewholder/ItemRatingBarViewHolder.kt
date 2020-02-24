package com.tokopedia.reviewseller.feature.reviewdetail.view.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.reviewseller.R
import com.tokopedia.reviewseller.feature.reviewdetail.view.model.ItemRatingBarModel
import kotlinx.android.synthetic.main.item_rating_bar_review_detail.view.*
import kotlinx.android.synthetic.main.item_rating_bar_review_detail.view.rating_star_value

class ItemRatingBarViewHolder(val view: View): RecyclerView.ViewHolder(view) {

    companion object{
        @JvmStatic
        val LAYOUT = R.layout.item_rating_bar_review_detail
        private const val DEFAULT_RATING_BAR_VALUE = 0
    }

    fun bind(element: ItemRatingBarModel?) {
        view.rating_star_value.text = element?.starPosition
        view.rating_total_review.text = "(${element?.totalReview})"
        view.progress_bar_rating.setValue(0, true)
    }
}
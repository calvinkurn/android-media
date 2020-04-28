package com.tokopedia.reviewseller.feature.reviewdetail.view.adapter.viewholder

import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.reviewseller.R
import com.tokopedia.reviewseller.feature.reviewdetail.view.adapter.SellerRatingAndTopicListener
import com.tokopedia.reviewseller.feature.reviewdetail.view.model.RatingBarUiModel
import kotlinx.android.synthetic.main.item_rating_bar_review_detail.view.*

class RatingAndTopicDetailViewHolder(val view: View, val listener: SellerRatingAndTopicListener) : RecyclerView.ViewHolder(view) {


    companion object {
        @JvmStatic
        val LAYOUT = R.layout.item_rating_bar_review_detail
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    fun bind(element: RatingBarUiModel) {
        with(view) {
            rating_checkbox.setOnCheckedChangeListener(null)
            rating_checkbox.isChecked = element.ratingIsChecked

            rating_checkbox.setOnCheckedChangeListener { _, isChecked ->
                if (element.ratingLabel != -1 && element.ratingLabel != null && isChecked != element.ratingIsChecked) {
                    listener.onRatingCheckBoxClicked(element.ratingLabel!! to isChecked, adapterPosition)
                }
            }

            rating_star_label.text = element.ratingLabel.toString()
            rating_total_review.text = String.format("(${element.ratingCount})")
            progress_bar_rating.apply {
                setValue(element.ratingProgressBar.toInt(), true)
            }
        }
    }
}
package com.tokopedia.review.feature.reviewdetail.view.adapter.viewholder

import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.review.R
import com.tokopedia.review.feature.reviewdetail.view.adapter.SellerRatingAndTopicListener
import com.tokopedia.review.feature.reviewdetail.view.model.RatingBarUiModel
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
            setupRatingCheckbox(element)

            rating_checkbox.setOnCheckedChangeListener { _, isChecked ->
                if (element.ratingLabel != 0 && element.ratingLabel != null && isChecked != element.ratingIsChecked) {
                    listener.onRatingCheckBoxClicked(
                            element.ratingLabel.orZero() to isChecked,
                            element.ratingLabel.orZero(),
                            adapterPosition)
                }
            }

            rating_star_label.text = element.ratingLabel.toString()
            rating_total_review.text = String.format("(${element.ratingCount})")
            progress_bar_rating.apply {
                setValue(element.ratingProgressBar.toInt(), true)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun setupRatingCheckbox(element: RatingBarUiModel) = with(view) {
        if (element.ratingCount == 0) {
            rating_star_icon.setImageResource(R.drawable.ic_rating_star_inactive)
            rating_star_label.setTextColor(ContextCompat.getColor(view.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_44))
            rating_checkbox.isEnabled = false
        } else {
            rating_star_icon.setImageResource(R.drawable.ic_rating_star_item)
            rating_star_label.setTextColor(ContextCompat.getColor(view.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_96))
            rating_checkbox.isEnabled = true
            rating_checkbox.isChecked = element.ratingIsChecked
        }
    }
}
package com.tokopedia.review.feature.reviewdetail.view.adapter.viewholder

import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.review.R
import com.tokopedia.review.databinding.ItemRatingBarReviewDetailBinding
import com.tokopedia.review.feature.reviewdetail.view.adapter.SellerRatingAndTopicListener
import com.tokopedia.review.feature.reviewdetail.view.model.RatingBarUiModel

class RatingAndTopicDetailViewHolder(view: View, val listener: SellerRatingAndTopicListener) : RecyclerView.ViewHolder(view) {

    companion object {
        @JvmStatic
        val LAYOUT = R.layout.item_rating_bar_review_detail
    }

    private val binding = ItemRatingBarReviewDetailBinding.bind(view)

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    fun bind(element: RatingBarUiModel) {
        with(binding) {
            ratingCheckbox.setOnCheckedChangeListener(null)
            setupRatingCheckbox(element)

            ratingCheckbox.setOnCheckedChangeListener { _, isChecked ->
                if (element.ratingLabel != 0 && element.ratingLabel != null && isChecked != element.ratingIsChecked) {
                    listener.onRatingCheckBoxClicked(
                            element.ratingLabel.orZero() to isChecked,
                            element.ratingLabel.orZero(),
                            adapterPosition)
                }
            }

            ratingStarLabel.text = element.ratingLabel.toString()
            ratingTotalReview.text = String.format("(${element.ratingCount})")
            progressBarRating.apply {
                setValue(element.ratingProgressBar.toInt(), true)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun setupRatingCheckbox(element: RatingBarUiModel) = with(binding) {
        if (element.ratingCount == 0) {
            ratingStarIcon.setImageResource(com.tokopedia.reviewcommon.R.drawable.ic_rating_star_inactive)
            ratingStarLabel.setTextColor(ContextCompat.getColor(root.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_44))
            ratingCheckbox.isEnabled = false
        } else {
            ratingStarIcon.setImageResource(com.tokopedia.reviewcommon.R.drawable.ic_rating_star_item)
            ratingStarLabel.setTextColor(ContextCompat.getColor(root.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_96))
            ratingCheckbox.isEnabled = true
            ratingCheckbox.isChecked = element.ratingIsChecked
        }
    }
}
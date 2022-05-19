package com.tokopedia.review.feature.media.detail.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.review.databinding.WidgetReviewDetailRatingBinding
import com.tokopedia.unifycomponents.BaseCustomView

class ReviewDetailRating @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseCustomView(context, attrs, defStyleAttr) {
    private val binding = WidgetReviewDetailRatingBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    private fun IconUnify.updateStar(active: Boolean) {
        isEnabled = active
    }

    fun setRating(rating: Int) {
        with(binding) {
            icReviewDetailRatingOne.updateStar(rating >= 1)
            icReviewDetailRatingTwo.updateStar(rating >= 2)
            icReviewDetailRatingThree.updateStar(rating >= 3)
            icReviewDetailRatingFour.updateStar(rating >= 4)
            icReviewDetailRatingFive.updateStar(rating >= 5)
        }
    }
}
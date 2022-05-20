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

    companion object {
        private const val RATING_ONE = 1
        private const val RATING_TWO = 2
        private const val RATING_THREE = 3
        private const val RATING_FOUR = 4
        private const val RATING_FIVE = 5
    }

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
            icReviewDetailRatingOne.updateStar(rating >= RATING_ONE)
            icReviewDetailRatingTwo.updateStar(rating >= RATING_TWO)
            icReviewDetailRatingThree.updateStar(rating >= RATING_THREE)
            icReviewDetailRatingFour.updateStar(rating >= RATING_FOUR)
            icReviewDetailRatingFive.updateStar(rating >= RATING_FIVE)
        }
    }
}
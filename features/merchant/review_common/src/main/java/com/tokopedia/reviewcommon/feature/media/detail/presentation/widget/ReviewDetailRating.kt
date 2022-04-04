package com.tokopedia.reviewcommon.feature.media.detail.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.reviewcommon.databinding.WidgetReviewDetailRatingBinding
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
        val color = if (active) {
            MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_YN300)
        } else {
            MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN300)
        }
        setImage(IconUnify.STAR_FILLED, color, color, color, color)
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
package com.tokopedia.reviewcommon.feature.media.detail.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.kotlin.extensions.view.showWithCondition
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

    fun setRating(rating: Int) {
        with(binding) {
            icReviewDetailRatingOneActive.showWithCondition(rating >= 1)
            icReviewDetailRatingTwoActive.showWithCondition(rating >= 2)
            icReviewDetailRatingThreeActive.showWithCondition(rating >= 3)
            icReviewDetailRatingFourActive.showWithCondition(rating >= 4)
            icReviewDetailRatingFiveActive.showWithCondition(rating >= 5)
            icReviewDetailRatingOneInactive.showWithCondition(rating < 1)
            icReviewDetailRatingTwoInactive.showWithCondition(rating < 2)
            icReviewDetailRatingThreeInactive.showWithCondition(rating < 3)
            icReviewDetailRatingFourInactive.showWithCondition(rating < 4)
            icReviewDetailRatingFiveInactive.showWithCondition(rating < 5)
        }
    }
}
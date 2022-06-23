package com.tokopedia.reviewcommon.util

import com.tokopedia.reviewcommon.R
import com.tokopedia.reviewcommon.constant.ReviewCommonConstants

fun getReviewStar(ratingCount: Int): Int {
    return when (ratingCount) {
        ReviewCommonConstants.RATING_1 -> {
            R.drawable.review_ic_rating_star_one
        }
        ReviewCommonConstants.RATING_2 -> {
            R.drawable.review_ic_rating_star_two
        }
        ReviewCommonConstants.RATING_3 -> {
            R.drawable.review_ic_rating_star_three
        }
        ReviewCommonConstants.RATING_4 -> {
            R.drawable.review_ic_rating_star_four
        }
        ReviewCommonConstants.RATING_5 -> {
            R.drawable.review_ic_rating_star_five
        }
        else -> {
            R.drawable.review_ic_rating_star_zero
        }
    }
}
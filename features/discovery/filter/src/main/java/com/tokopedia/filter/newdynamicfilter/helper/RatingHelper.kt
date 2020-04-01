package com.tokopedia.filter.newdynamicfilter.helper

import com.tokopedia.filter.R

object RatingHelper {
    fun getRatingDrawable(ratingCount: Int): Int {
        return when (ratingCount) {
            0 -> R.drawable.ic_star_none
            1 -> R.drawable.ic_star_one
            2 -> R.drawable.ic_star_two
            3 -> R.drawable.ic_star_three
            4 -> R.drawable.ic_star_four
            5 -> R.drawable.ic_star_five
            else -> R.drawable.ic_star_none
        }
    }
}

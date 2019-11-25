package com.tokopedia.filter.newdynamicfilter.helper

import com.tokopedia.filter.R

object RatingHelper {
    fun getRatingDrawable(ratingCount: Int): Int {
        when (ratingCount) {
            0 -> return R.drawable.ic_star_none
            1 -> return R.drawable.ic_star_one
            2 -> return R.drawable.ic_star_two
            3 -> return R.drawable.ic_star_three
            4 -> return R.drawable.ic_star_four
            5 -> return R.drawable.ic_star_five
            else -> return R.drawable.ic_star_none
        }
    }
}

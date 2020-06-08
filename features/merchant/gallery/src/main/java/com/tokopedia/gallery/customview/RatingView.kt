package com.tokopedia.gallery.customview

import com.tokopedia.gallery.R

/**
 * @author Angga.Prasetiyo on 23/10/2015.
 */
object RatingView {

    fun getRatingDrawable(param: Int): Int {
        return when (param) {
            0 -> R.drawable.ic_gold_star_none
            1 -> R.drawable.ic_gold_star_one
            2 -> R.drawable.ic_gold_star_two
            3 -> R.drawable.ic_gold_star_three
            4 -> R.drawable.ic_gold_star_four
            5 -> R.drawable.ic_gold_star_five
            else -> R.drawable.ic_gold_star_none
        }
    }
}

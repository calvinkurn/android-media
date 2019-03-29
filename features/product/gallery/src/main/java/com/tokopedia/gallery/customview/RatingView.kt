package com.tokopedia.gallery.customview

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView

import com.tokopedia.gallery.R

/**
 * @author Angga.Prasetiyo on 23/10/2015.
 */
object RatingView {

    fun getRatingDrawable(param: Int): Int {
        when (param) {
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

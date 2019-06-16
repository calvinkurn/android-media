package com.tokopedia.sellerapp.dashboard.view.widget

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.support.v4.content.ContextCompat
import android.util.AttributeSet

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar
import com.tokopedia.seller.R

class RoundGradientProgressBar : RoundCornerProgressBar {
    private var progressColors: IntArray? = null

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    override fun createGradientDrawable(color: Int): GradientDrawable {
        val gradientDrawable: GradientDrawable
        if (progressColors == null) {
            progressColors = intArrayOf(ContextCompat.getColor(context, R.color.lightish_green), ContextCompat.getColor(context, R.color.tkpd_main_green))
        }
        if (color in progressColors!!) {
            gradientDrawable = GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, progressColors)
        } else {
            gradientDrawable = GradientDrawable()
            gradientDrawable.setColor(color)
        }
        gradientDrawable.shape = GradientDrawable.RECTANGLE
        return gradientDrawable
    }

    fun setProgressColor(colorProgress: IntArray) {
        super.setProgressColor(colorProgress[0])
    }

}

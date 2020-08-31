package com.tokopedia.sellerhome.view.widget

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar


/**
 * Created by hendry on 2019-06-16.
 */
class RoundGradientProgressBar : RoundCornerProgressBar {
    private var progressColors: IntArray? = null

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun createGradientDrawable(color: Int): GradientDrawable {
        val gradientDrawable: GradientDrawable
        var existInProgressColor = false
        progressColors?.run {
            for (colorItem in this) {
                if (color == colorItem) {
                    existInProgressColor = true
                    break
                }
            }
        }
        if (existInProgressColor) {
            gradientDrawable = GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, progressColors)
        } else {
            gradientDrawable = GradientDrawable()
            gradientDrawable.setColor(color)
        }
        gradientDrawable.shape = GradientDrawable.RECTANGLE

        return gradientDrawable
    }

    fun setProgressColor(progressColors: IntArray) {
        this.progressColors = progressColors
        super.setProgressColor(progressColors.first())
    }
}


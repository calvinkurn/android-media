package com.tokopedia.shop.score.detail_old.view.widget

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.widget.LinearLayout
import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar

class SquareProgressBar : RoundCornerProgressBar {
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    override fun drawProgress(
        layoutProgress: LinearLayout,
        max: Float,
        progress: Float,
        totalWidth: Float,
        radius: Int,
        padding: Int,
        colorProgress: Int,
        isReverse: Boolean) {
        val backgroundDrawable = createGradientDrawable(colorProgress)
        val newRadius = radius - padding / 2
        val rightRadius: Float = if (progress / max < LIMIT_ROUNDED) 0f else newRadius.toFloat()
        backgroundDrawable.cornerRadii = floatArrayOf(
            newRadius.toFloat(),
            newRadius.toFloat(),
            rightRadius,
            rightRadius,
            rightRadius,
            rightRadius,
            newRadius.toFloat(),
            newRadius.toFloat())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            layoutProgress.background = backgroundDrawable
        } else {
            layoutProgress.setBackgroundDrawable(backgroundDrawable)
        }
        val ratio = max / progress
        val progressWidth = ((totalWidth - padding * 2) / ratio).toInt()
        val progressParams = layoutProgress.layoutParams
        progressParams.width = progressWidth
        layoutProgress.layoutParams = progressParams
    }

    companion object {
        const val LIMIT_ROUNDED = 0.98
    }
}
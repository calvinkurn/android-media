package com.tokopedia.home_component.customview.bannerindicator

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.animation.LinearInterpolator
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.appcompat.view.ContextThemeWrapper
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by dhaba
 */
class BannerIndicator : LinearLayout {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        val progressBarTheme = ContextThemeWrapper(context, com.tokopedia.home_component.R.style.IndicatorBanner)
        val progress = ProgressBar(progressBarTheme, null, 0)
        this.addView(progress)
        animateIndicatorBanner(progress)
    }

    private fun animateIndicatorBanner(progressIndicator: ProgressBar) {
        val slideAnimator = ValueAnimator
            .ofInt(0, 100)
            .setDuration(5000)
        slideAnimator.addUpdateListener { animation ->
            val value = animation.animatedValue as Int
            progressIndicator.progress = value
        }
        val set = AnimatorSet()
        set.play(slideAnimator)
        set.interpolator = LinearInterpolator()
        set.start()
    }
}

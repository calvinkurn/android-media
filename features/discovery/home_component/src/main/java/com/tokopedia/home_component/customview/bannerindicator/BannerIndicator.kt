package com.tokopedia.home_component.customview.bannerindicator

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.animation.LinearInterpolator
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.appcompat.view.ContextThemeWrapper
import com.tokopedia.kotlin.extensions.view.ZERO

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

    private val set = AnimatorSet()

    private fun addProgressBar(tag: Int) {
        val progressBarTheme = ContextThemeWrapper(context, com.tokopedia.home_component.R.style.IndicatorBanner)
        val progress = ProgressBar(progressBarTheme, null, Int.ZERO)
        progress.tag = tag
        this.addView(progress)
        animateIndicatorBanner(progress)
    }

    fun pauseAnimation() {
        set.pause()
    }

    fun continueAnimation() {
        set.resume()
    }

    fun setBannerIndicators(totalBanner: Int) {
        if (totalBanner > Int.ZERO) {
            for (i in Int.ZERO until totalBanner) {
                addProgressBar(i)
            }
        }
    }

    private fun animateIndicatorBanner(progressIndicator: ProgressBar) {
        val slideAnimator = ValueAnimator
            .ofInt(0, 100)
            .setDuration(5000)
        slideAnimator.addUpdateListener { animation ->
            val value = animation.animatedValue as Int
            progressIndicator.progress = value
        }
        set.play(slideAnimator)
        set.interpolator = LinearInterpolator()
        set.start()
    }
}

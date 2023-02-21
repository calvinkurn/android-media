package com.tokopedia.home_component.customview.bannerindicator

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.animation.LinearInterpolator
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.appcompat.view.ContextThemeWrapper
import com.tokopedia.home_component.customview.pullrefresh.LayoutIconPullRefreshView
import com.tokopedia.home_component.util.toDpInt
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.unifycomponents.toDp
import com.tokopedia.unifycomponents.toPx

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
    private var totalBanner = Int.ZERO

    init {
        this.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)

    }

    private var listener: BannerIndicatorListener? = null

    fun setBannerListener(listener: BannerIndicatorListener) {
        this.listener = listener
    }

    private fun addProgressBar(tag: Int) {
        val progressBarTheme = ContextThemeWrapper(context, com.tokopedia.home_component.R.style.IndicatorBanner)
        val progress = ProgressBar(progressBarTheme, null, Int.ZERO)
        progress.tag = tag
        this.addView(progress)
        val layoutParams = progress.layoutParams as LayoutParams
        layoutParams.marginStart = 2f.toDpInt()
        layoutParams.marginEnd = 2f.toDpInt()
        layoutParams.width = 6f.toDpInt()
        layoutParams.height = 6f.toDpInt()
        progress.layoutParams = layoutParams
    }

    fun pauseAnimation() {
        set.pause()
    }

    fun continueAnimation() {
        set.resume()
    }

    fun setBannerIndicators(totalBanner: Int) {
        this.totalBanner = totalBanner
        if (totalBanner > Int.ZERO) {
            for (i in Int.ZERO until totalBanner) {
                addProgressBar(i)
            }
            animateIndicatorBanner(this.getChildAt(Int.ZERO) as ProgressBar, Int.ZERO)
        }
    }

    private fun animateIndicatorBanner(progressIndicator: ProgressBar, position: Int) {
        val layoutParams = progressIndicator.layoutParams
        layoutParams.width = 46f.toDpInt()
        progressIndicator.layoutParams = layoutParams
        val slideAnimator = ValueAnimator
            .ofInt(0, 100)
            .setDuration(5000)
        slideAnimator.addUpdateListener { animation ->
            val value = animation.animatedValue as Int
            progressIndicator.progress = value
            if (value >= 100) {
                val nextTransition = if (position != totalBanner - Int.ZERO) {
                    position + Int.ONE
                } else {
                    Int.ZERO
                }
                listener?.onChangePosition(nextTransition)
                minimizeIndicatorBanner(progressIndicator)
            }
        }
        set.play(slideAnimator)
        set.interpolator = LinearInterpolator()
        set.start()
    }

    private fun minimizeIndicatorBanner(progressIndicator: ProgressBar) {
        progressIndicator.progress = Int.ZERO
        val slideAnimator = ValueAnimator
            .ofInt(46, 6)
            .setDuration(600)
        slideAnimator.addUpdateListener { animation ->
            val value = animation.animatedValue as Int
            progressIndicator.layoutParams?.width = value.toPx()
            progressIndicator.requestLayout()
        }
        val hideAnimatorSet = AnimatorSet()
        hideAnimatorSet.play(slideAnimator)
        hideAnimatorSet.interpolator = LinearInterpolator()
        hideAnimatorSet.start()
    }
}

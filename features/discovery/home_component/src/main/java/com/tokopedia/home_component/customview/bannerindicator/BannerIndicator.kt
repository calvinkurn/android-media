package com.tokopedia.home_component.customview.bannerindicator

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.os.Looper
import android.util.AttributeSet
import android.view.animation.LinearInterpolator
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.appcompat.view.ContextThemeWrapper
import com.tokopedia.home_component.util.toDpInt
import com.tokopedia.home_component.viewholders.BannerRevampViewHolder
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
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

    private val bannerAnimatorSet = AnimatorSet()
    private var bannerAnimator = ValueAnimator
        .ofInt(0, 100)
        .setDuration(5000)
    private var totalBanner = Int.ZERO
    private var currentPosition = Int.ZERO

    companion object {
        private const val MINIMUM_PROGRESS = 0
        private const val MAXIMUM_PROGRESS = 100
    }

    init {
        this.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
    }

    private var listener: BannerIndicatorListener? = null

    fun setBannerListener(listener: BannerIndicatorListener) {
        this.listener = listener
    }

    private fun addProgressBar(tag: Int) {
        val progressBarTheme =
            ContextThemeWrapper(context, com.tokopedia.home_component.R.style.IndicatorBanner)
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
        bannerAnimatorSet.pause()
    }

    fun continueAnimation() {
        bannerAnimatorSet.resume()
    }

    fun setBannerIndicators(totalBanner: Int) {
        this.removeAllViews()
        this.totalBanner = totalBanner
        if (totalBanner > Int.ZERO) {
            for (i in Int.ZERO until totalBanner) {
                addProgressBar(i)
            }
            initialAnimate(this.getChildAt(Int.ZERO) as ProgressBar, Int.ZERO)
        }
    }

    private fun maximizeAnimator(progressIndicator: ProgressBar, position: Int) {
        val maximizeAnimator = ValueAnimator
            .ofInt(6, 46)
            .setDuration(BannerRevampViewHolder.FLING_DURATION.toLong())
        maximizeAnimator.addUpdateListener { animation ->
            val value = animation.animatedValue as Int
            progressIndicator.layoutParams?.width = value.toPx()
            progressIndicator.requestLayout()
            if (value >= 46) {
                animateIndicatorBanner(progressIndicator, position)
            }
        }

        val maxAnimatorSet = AnimatorSet()
        maxAnimatorSet.play(maximizeAnimator)
        maxAnimatorSet.interpolator = LinearInterpolator()
        maxAnimatorSet.start()
    }

    private fun initialAnimate(progressIndicator: ProgressBar, position: Int) {
        val layoutParams = progressIndicator.layoutParams
        layoutParams.width = 46f.toDpInt()
        progressIndicator.layoutParams = layoutParams

        animateIndicatorBanner(progressIndicator, position)
    }

    private fun animateIndicatorBanner(progressIndicator: ProgressBar, position: Int) {
        currentPosition = position
        bannerAnimator.addUpdateListener { animation ->
            val value = animation.animatedValue as Int
            progressIndicator.progress = value
            if (value >= 100) {
//                val nextTransition = if (position != totalBanner - Int.ONE) {
//                    position + Int.ONE
//                } else {
//                    Int.ZERO
//                }
                val nextTransition = position + Int.ONE
                listener?.onChangePosition(nextTransition)
                android.os.Handler(Looper.getMainLooper()).postDelayed(
                    {
                        bannerAnimatorSet.removeAllListeners()
                        bannerAnimator.removeAllUpdateListeners()
                        minimizeIndicatorBanner(progressIndicator)
                        maximizeAnimator(
                            this.getChildAt(nextTransition % totalBanner) as ProgressBar,
                            nextTransition
                        )
                    },
                    0
                )
            }
        }
        bannerAnimatorSet.play(bannerAnimator)
        bannerAnimatorSet.interpolator = LinearInterpolator()
        bannerAnimatorSet.start()
    }

    private fun minimizeIndicatorBanner(progressIndicator: ProgressBar) {
        progressIndicator.progress = Int.ZERO
        val minimizeAnimator = ValueAnimator
            .ofInt(46, 6)
            .setDuration(BannerRevampViewHolder.FLING_DURATION.toLong())
        minimizeAnimator.addUpdateListener { animation ->
            val value = animation.animatedValue as Int
            progressIndicator.layoutParams?.width = value.toPx()
            progressIndicator.requestLayout()
        }
        val minAnimatorSet = AnimatorSet()
        minAnimatorSet.play(minimizeAnimator)
        minAnimatorSet.interpolator = LinearInterpolator()
        minAnimatorSet.start()
    }

    fun startIndicatorByPosition(position: Int) {
        bannerAnimatorSet.removeAllListeners()
        bannerAnimatorSet.cancel()
        bannerAnimator.removeAllUpdateListeners()
        val indicatorPosition = position % totalBanner
        if (position == currentPosition) {
            this.getChildAt(indicatorPosition)?.let {
                animateIndicatorBanner(it as ProgressBar, indicatorPosition)
            }
        } else {
            minimizeIndicatorBanner(this.getChildAt(currentPosition % totalBanner) as ProgressBar)
            this.getChildAt(indicatorPosition)?.let {
                maximizeAnimator(it as ProgressBar, indicatorPosition)
            }
        }
    }
}

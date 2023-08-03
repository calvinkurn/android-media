package com.tokopedia.home_component.customview.bannerindicator

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import android.provider.Settings
import android.util.AttributeSet
import android.view.animation.LinearInterpolator
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.appcompat.view.ContextThemeWrapper
import com.tokopedia.home_component.util.toDpInt
import com.tokopedia.home_component.viewholders.BannerComponentViewHolder
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

    companion object {
        private const val NO_DELAY = 0L
        private const val MINIMUM_PROGRESS = 0
        private const val MAXIMUM_PROGRESS = 100
        private const val SCROLL_TRANSITION_DURATION = 6000L
        private const val WIDTH_MINIMUM_PROGRESS = 6
        private const val WIDTH_MAXIMUM_PROGRESS = 48
        private const val MINIMUM_PROGRESS_ALPHA = 0
        private const val MAXIMUM_PROGRESS_ALPHA = 1f
    }

    var isInitialized = false

    private val marginHorizontalProgress = 2f.toDpInt()
    private val sizeMinimizeProgress = 6f.toDpInt()

    private val bannerAnimatorSet = AnimatorSet()
    private var bannerAnimator = ValueAnimator
        .ofInt(MINIMUM_PROGRESS, MAXIMUM_PROGRESS)
        .setDuration(SCROLL_TRANSITION_DURATION)
    private var totalBanner = Int.ZERO
    private var currentPosition = Int.ZERO
    private var nextTransition = 0

    init {
        this.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
    }

    private var listener: BannerIndicatorListener? = null

    fun setBannerListener(listener: BannerIndicatorListener) {
        this.listener = listener
    }

    @SuppressLint("UnifyComponentUsage")
    private fun addProgressBar() {
        val progressBarTheme =
            ContextThemeWrapper(context, com.tokopedia.home_component.R.style.IndicatorBanner)
        val progress = ProgressBar(progressBarTheme, null, Int.ZERO)
        this.addView(progress)
        val layoutParams = progress.layoutParams as LayoutParams
        layoutParams.marginStart = marginHorizontalProgress
        layoutParams.marginEnd = marginHorizontalProgress
        layoutParams.width = sizeMinimizeProgress
        layoutParams.height = sizeMinimizeProgress
        progress.layoutParams = layoutParams
    }

    fun pauseAnimation() {
        bannerAnimatorSet.pause()
    }

    fun continueAnimation() {
        bannerAnimatorSet.resume()
    }

    fun setBannerIndicators(totalBanner: Int, startFrom: Int = -1) {
        this.removeAllViews()
        this.totalBanner = totalBanner
        if (totalBanner > Int.ONE) {
            var start = 0
            if (startFrom > -1) {
                cancelAllAnimation()
                start = startFrom
            }
            repeat(totalBanner) {
                addProgressBar()
            }
            getChildProgressBar(start)?.let {
                initialAnimate(it, start)
            }
        } else {
            this.removeAllViews()
        }
        isInitialized = true
    }

    private fun cancelAllAnimation() {
        bannerAnimatorSet.removeAllListeners()
        bannerAnimatorSet.cancel()
        bannerAnimator.removeAllUpdateListeners()
        bannerAnimator.cancel()
    }

    private fun maximizeAnimator(progressIndicator: ProgressBar, position: Int) {
        val maximizeAnimator = ValueAnimator
            .ofInt(WIDTH_MINIMUM_PROGRESS, WIDTH_MAXIMUM_PROGRESS)
            .setDuration(BannerComponentViewHolder.FLING_DURATION.toLong())
        maximizeAnimator.addUpdateListener { animation ->
            val value = animation.animatedValue as Int
            progressIndicator.layoutParams?.width = value.toPx()
            progressIndicator.requestLayout()
            if (value >= WIDTH_MAXIMUM_PROGRESS) {
                animateIndicatorBanner(progressIndicator, position)
            }
        }
        val maxAnimatorSet = AnimatorSet()
        maxAnimatorSet.play(maximizeAnimator)
        maxAnimatorSet.interpolator = BannerComponentViewHolder.autoScrollInterpolator
        maxAnimatorSet.start()
    }

    private fun initialAnimate(progressIndicator: ProgressBar, position: Int) {
        val layoutParams = progressIndicator.layoutParams
        layoutParams.width = WIDTH_MAXIMUM_PROGRESS.toPx()
        progressIndicator.layoutParams = layoutParams
        animateIndicatorBanner(progressIndicator, position)
    }

    private fun animateIndicatorBanner(progressIndicator: ProgressBar, position: Int) {
        currentPosition = position
        if(!isAnimationTurnedOff()) {
            bannerAnimator.addUpdateListener { animation ->
                val value = animation.animatedValue as Int
                progressIndicator.progress = value
                if (value >= MAXIMUM_PROGRESS) {
                    nextTransition = if (position != Int.MAX_VALUE - Int.ONE) {
                        position + Int.ONE
                    } else {
                        Int.ZERO
                    }
                    android.os.Handler(Looper.getMainLooper()).postDelayed(
                        {
                            bannerAnimatorSet.removeAllListeners()
                            bannerAnimator.removeAllUpdateListeners()
                            minimizeIndicatorBanner(progressIndicator)
                            getChildProgressBar(nextTransition % totalBanner)?.let {
                                maximizeAnimator(it, nextTransition)
                            }
                            listener?.onChangePosition(nextTransition)
                        },
                        NO_DELAY
                    )
                }
            }
            bannerAnimatorSet.play(bannerAnimator)
            bannerAnimatorSet.interpolator = LinearInterpolator()
            bannerAnimatorSet.start()
        }
    }

    private fun minimizeIndicatorBanner(progressIndicator: ProgressBar) {
        val minimizeAnimator = ValueAnimator
            .ofInt(WIDTH_MAXIMUM_PROGRESS, WIDTH_MINIMUM_PROGRESS)
            .setDuration(BannerComponentViewHolder.FLING_DURATION.toLong())
        minimizeAnimator.addUpdateListener { animation ->
            val value = animation.animatedValue as Int
            progressIndicator.layoutParams?.width = value.toPx()
            progressIndicator.requestLayout()
            if (value == WIDTH_MINIMUM_PROGRESS) {
                progressIndicator.progress = MINIMUM_PROGRESS_ALPHA
                progressIndicator.alpha = MAXIMUM_PROGRESS_ALPHA
            }
        }
        val minAnimatorSet = AnimatorSet()
        minAnimatorSet.play(minimizeAnimator)
        minAnimatorSet.interpolator = BannerComponentViewHolder.autoScrollInterpolator
        minAnimatorSet.start()
    }

    fun startIndicatorByPosition(position: Int) {
        if (bannerAnimator.isRunning) {
            bannerAnimatorSet.removeAllListeners()
            bannerAnimatorSet.cancel()
            bannerAnimator.removeAllUpdateListeners()
            bannerAnimator.cancel()
        }
        val indicatorPosition = position % totalBanner
        try {
            if (indicatorPosition == currentPosition) {
                getChildProgressBar(indicatorPosition)?.let {
                    it.progress = Int.ZERO
                    animateIndicatorBanner(it, indicatorPosition)
                }
            } else {
                getChildProgressBar(currentPosition % totalBanner)?.let {
                    minimizeIndicatorBanner(it)
                }
                getChildProgressBar(indicatorPosition)?.let {
                    maximizeAnimator(it, indicatorPosition)
                }
            }
        } catch (_: Exception) {
            // no-op
        }
    }

    private fun getChildProgressBar(position: Int): ProgressBar? {
        return try {
            this.getChildAt(position) as ProgressBar
        } catch (_: Exception) {
            null
        }
    }
    private fun isAnimationTurnedOff(): Boolean {
        return try {
            Settings.Global.getFloat(context.contentResolver, Settings.Global.ANIMATOR_DURATION_SCALE) == 0f
        } catch (e: Settings.SettingNotFoundException) {
            true
        }
    }
}

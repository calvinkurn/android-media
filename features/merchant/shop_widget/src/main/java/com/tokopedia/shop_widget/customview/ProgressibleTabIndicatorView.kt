package com.tokopedia.shop_widget.customview

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.core.animation.addListener
import androidx.core.content.ContextCompat
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.shop_widget.R
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.UnifyMotion

class ProgressibleTabIndicatorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), DefaultLifecycleObserver {

    init {
        orientation = HORIZONTAL
    }
    private var widthAnimator: ValueAnimator? = null
    private var progressAnimator: ValueAnimator? = null
    private var tabIndicatorCount = 0

    companion object {
        private const val UNSELECTED_TAB_INDICATOR_HEIGHT = 5
        private const val UNSELECTED_TAB_INDICATOR_WIDTH = 5

        private const val DOT_INDICATOR_MARGIN_START = 3

        private const val ANIMATE_PROGRESSBAR_WIDTH_DURATION_MILLIS: Long = 600
        private const val ANIMATE_PROGRESSBAR_PROGRESS_DURATION_MILLIS: Long = 6000

        private const val SELECTED_TAB_INDICATOR_HEIGHT = 5
        private const val SELECTED_TAB_INDICATOR_MIN_WIDTH = 6
        private const val SELECTED_TAB_INDICATOR_MAX_WIDTH = 28

        private const val SELECTED_TAB_INDICATOR_MIN_PROGRESS = 0
        private const val SELECTED_TAB_INDICATOR_MAX_PROGRESS = 100
    }

    fun initWithLifecycle(
        tabIndicatorCount: Int,
        lifecycle: Lifecycle
    ) {
        this.tabIndicatorCount = tabIndicatorCount
        lifecycle.addObserver(this)

        try {
            showTabIndicator(tabIndicatorCount = tabIndicatorCount, selectedPosition = Int.ZERO)
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().recordException(e)
        }
    }

    private fun showTabIndicator(
        tabIndicatorCount: Int,
        selectedPosition: Int
    ) {
        // Remove existing views before recreate new tab indicators
        removeAllViews()

        cancelAnimation()

        for (currentIndex in 0 until tabIndicatorCount) {
            if (currentIndex == selectedPosition) {
                addView(createProgressBar())
            } else {
                addView(createUnselectedDot())
            }
        }
    }

    fun setIndicatorActive(selectedIndex: Int) {
        val progressBar = createProgressBar()

        removeViewAt(selectedIndex)
        addView(progressBar, selectedIndex)
        progressBar.animated()

        setIndicatorInactive(selectedIndex, tabIndicatorCount)
    }

    private fun setIndicatorInactive(selectedIndex: Int, tabIndicatorCount: Int) {
        for (currentIndex in 0 until tabIndicatorCount) {
            if (currentIndex != selectedIndex) {
                removeViewAt(currentIndex)
                addView(createUnselectedDot(), currentIndex)
            }
        }
    }

    private fun ProgressBar.animated() {
        widthAnimator?.cancel()
        progressAnimator?.cancel()

        widthAnimator = ValueAnimator.ofInt(SELECTED_TAB_INDICATOR_MIN_WIDTH, SELECTED_TAB_INDICATOR_MAX_WIDTH)
        widthAnimator?.duration = ANIMATE_PROGRESSBAR_WIDTH_DURATION_MILLIS
        widthAnimator?.interpolator = UnifyMotion.EASE_IN_OUT
        widthAnimator?.addUpdateListener { animation ->
            val value = animation.animatedValue as Int

            val updatedLayoutParams = layoutParams
            layoutParams?.width = value.toPx()
            layoutParams = updatedLayoutParams
        }

        widthAnimator?.addListener(onEnd = {
            progressAnimator = ValueAnimator.ofInt(SELECTED_TAB_INDICATOR_MIN_PROGRESS, SELECTED_TAB_INDICATOR_MAX_PROGRESS)
            progressAnimator?.duration = ANIMATE_PROGRESSBAR_PROGRESS_DURATION_MILLIS
            progressAnimator?.interpolator = LinearInterpolator()
            progressAnimator?.addUpdateListener { animation ->
                progress = animation.animatedValue as Int
            }
            progressAnimator?.start()
        })

        widthAnimator?.start()
    }

    @SuppressLint("UnifyComponentUsage")
    private fun createProgressBar(): ProgressBar {
        val progressBar = ProgressBar(
            context,
            null,
            android.R.attr.progressBarStyleHorizontal
        ).apply {
            layoutParams = LayoutParams(
                SELECTED_TAB_INDICATOR_MIN_WIDTH.toPx(),
                SELECTED_TAB_INDICATOR_HEIGHT.toPx()
            ).apply {
                leftMargin = DOT_INDICATOR_MARGIN_START.toPx()
            }
            max = SELECTED_TAB_INDICATOR_MAX_PROGRESS
            progress = Int.ZERO
        }

        val backgroundDrawable = ContextCompat.getDrawable(
            context,
            R.drawable.shape_progressible_tab_indicator_progressbar_background
        )
        val progressDrawable = ContextCompat.getDrawable(
            context,
            R.drawable.shape_progressible_tab_indicator_progressbar_progress
        )

        progressBar.progressDrawable = progressDrawable
        progressBar.background = backgroundDrawable

        return progressBar
    }

    private fun createUnselectedDot(): ImageView {
        val imageView = ImageView(context)
        val params = LayoutParams(
            UNSELECTED_TAB_INDICATOR_WIDTH.toPx(),
            UNSELECTED_TAB_INDICATOR_HEIGHT.toPx()
        )
        params.leftMargin = DOT_INDICATOR_MARGIN_START.toPx()
        imageView.layoutParams = params

        imageView.setImageDrawable(
            ContextCompat.getDrawable(
                context,
                R.drawable.shape_progressible_tab_indicator_unselected
            )
        )

        return imageView
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        resumeAnimation()
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        pauseAnimation()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        cancelAnimation()
    }

    private fun resumeAnimation() {
        widthAnimator?.resume()
        progressAnimator?.resume()
    }

    private fun pauseAnimation() {
        widthAnimator?.pause()
        progressAnimator?.pause()
    }

    private fun cancelAnimation() {
        widthAnimator?.cancel()
        progressAnimator?.cancel()

        widthAnimator = null
        progressAnimator = null
    }
}

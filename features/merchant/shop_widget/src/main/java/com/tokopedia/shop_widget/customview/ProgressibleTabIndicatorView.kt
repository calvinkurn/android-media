package com.tokopedia.shop_widget.customview

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
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

    private var expandProgressBarWidthAnimatorSet: AnimatorSet? = null
    private var progressAnimatorSet: AnimatorSet? = null
    private var progressIndicatorValueAnimator: ValueAnimator? = null
    private var expandProgressBarWidthAnimator: ValueAnimator? = null

    companion object {
        private const val UNSELECTED_TAB_INDICATOR_HEIGHT = 5
        private const val UNSELECTED_TAB_INDICATOR_WIDTH = 5

        private const val DOT_INDICATOR_MARGIN_START = 3

        private const val SELECTED_TAB_INDICATOR_EXPAND_ANIMATION_DURATION_MILLIS: Long = 600
        private const val SELECTED_TAB_INDICATOR_PROGRESS_ANIMATION_DURATION_MILLIS: Long = 6000

        private const val SELECTED_TAB_INDICATOR_HEIGHT = 5
        private const val SELECTED_TAB_INDICATOR_MIN_WIDTH = 6
        private const val SELECTED_TAB_INDICATOR_MAX_WIDTH = 28

        private const val SELECTED_TAB_INDICATOR_MIN_PROGRESS = 0
        private const val SELECTED_TAB_INDICATOR_MAX_PROGRESS = 100
    }

    fun showTabIndicatorWithLifecycle(
        tabIndicatorCount: Int,
        selectedPosition: Int,
        lifecycle: Lifecycle,
        onProgressFinish: () -> Unit
    ) {
        lifecycle.addObserver(this)

        try {
            showTabIndicator(tabIndicatorCount, selectedPosition, onProgressFinish)
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().recordException(e)
        }
    }

    fun showTabIndicator(
        tabIndicatorCount: Int,
        selectedPosition: Int,
        onProgressFinish: () -> Unit
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

        expandSelectedTabIndicatorWidth(onProgressFinish = onProgressFinish)
    }

    private fun expandSelectedTabIndicatorWidth(onProgressFinish: () -> Unit) {
        val progressBar = getSelectedTabIndicator()
        if (progressBar == null) return

        expandProgressBarWidthAnimatorSet = AnimatorSet()

        expandProgressBarWidthAnimator =
            ValueAnimator.ofInt(SELECTED_TAB_INDICATOR_MIN_WIDTH, SELECTED_TAB_INDICATOR_MAX_WIDTH)
                .setDuration(SELECTED_TAB_INDICATOR_EXPAND_ANIMATION_DURATION_MILLIS)

        expandProgressBarWidthAnimator?.addUpdateListener { animation ->
            val value = animation.animatedValue as Int

            progressBar.layoutParams?.width = value.toPx()
            progressBar.requestLayout()

            if (value == SELECTED_TAB_INDICATOR_MAX_WIDTH) {
                animateSelectedTabIndicatorProgress(progressBar, onProgressFinish)
            }
        }

        expandProgressBarWidthAnimatorSet?.interpolator = UnifyMotion.EASE_IN_OUT
        expandProgressBarWidthAnimatorSet?.play(expandProgressBarWidthAnimator)
        expandProgressBarWidthAnimatorSet?.start()
    }

    private fun animateSelectedTabIndicatorProgress(
        progressBar: ProgressBar,
        onProgressFinish: () -> Unit
    ) {
        progressIndicatorValueAnimator = ValueAnimator
            .ofInt(SELECTED_TAB_INDICATOR_MIN_PROGRESS, SELECTED_TAB_INDICATOR_MAX_PROGRESS)
            .setDuration(SELECTED_TAB_INDICATOR_PROGRESS_ANIMATION_DURATION_MILLIS)

        progressAnimatorSet = AnimatorSet()

        progressIndicatorValueAnimator?.addUpdateListener { animation ->
            val value = animation.animatedValue as Int
            progressBar.progress = value
            if (value == SELECTED_TAB_INDICATOR_MAX_PROGRESS) {
                onProgressFinish()
            }
        }

        progressAnimatorSet?.interpolator = LinearInterpolator()
        progressAnimatorSet?.play(progressIndicatorValueAnimator)
        progressAnimatorSet?.start()
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
        expandProgressBarWidthAnimatorSet?.resume()
        progressAnimatorSet?.resume()
    }

    private fun pauseAnimation() {
        expandProgressBarWidthAnimatorSet?.pause()
        progressAnimatorSet?.pause()
    }

    private fun cancelAnimation() {
        expandProgressBarWidthAnimatorSet?.cancel()
        progressAnimatorSet?.cancel()

        expandProgressBarWidthAnimatorSet = null
        progressAnimatorSet = null
    }

    private fun getSelectedTabIndicator(): ProgressBar? {
        for (i in Int.ZERO..childCount) {
            val indicator = getChildAt(i)
            if (indicator is ProgressBar) {
                return indicator
            }
        }

        return null
    }
}

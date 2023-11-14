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
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.shop_widget.R
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.UnifyMotion

class ProgressibleTabLayoutView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), DefaultLifecycleObserver {

    private var onProgressFinish: (Int) -> Unit = {}

    private var expandProgressBarWidthAnimator: ValueAnimator? = null
    private var expandProgressBarWidthAnimatorSet: AnimatorSet? = null

    private var progressAnimatorSet: AnimatorSet? = null
    private var progressAnimator: ValueAnimator? = null
    private var config = Config(0, 6000, 1000)
    private var selectedPosition = 0

    init {
        orientation = HORIZONTAL
    }

    data class Config(
        val itemCount: Int,
        val totalDuration: Long,
        val intervalDuration: Long
    )

    companion object {
        private const val DOT_INDICATOR_INACTIVE_HEIGHT = 5
        private const val DOT_INDICATOR_INACTIVE_WIDTH = 5
        private const val DOT_INDICATOR_ACTIVE_HEIGHT = 5
        private const val DOT_INDICATOR_ACTIVE_WIDTH = 28
        private const val DOT_INDICATOR_MARGIN_START = 3
    }

    fun initializeWithLifecycle(
        config: Config,
        lifecycle: Lifecycle,
        onProgressFinish: (Int) -> Unit
    ) {
        lifecycle.addObserver(this)
        initialize(config, onProgressFinish)
    }

    fun initialize(
        config: Config,
        onProgressFinish: (Int) -> Unit
    ) {
        // Remove existing views before recreate new tab indicators
        removeAllViews()

        this.onProgressFinish = onProgressFinish
        this.config = config

        cancelAnimation()

        for (currentIndex in 0 until config.itemCount) {
            val item = if (currentIndex == selectedPosition) {
                createSelectedDot()
            } else {
                createUnselectedDot()
            }
            addView(item)
        }

        animateSelectedTabIndicator()
    }

    fun reset() {
        cancelAnimation()
        expandProgressBarWidthAnimator = null
        expandProgressBarWidthAnimatorSet = null

        progressAnimatorSet = null
        progressAnimator = null
    }

    fun select(newPosition: Int) {
        removeAllViews()

        this.selectedPosition = newPosition
        cancelAnimation()

        for (currentIndex in 0 until config.itemCount) {
            val item = if (currentIndex == newPosition) {
                createSelectedDot()
            } else {
                createUnselectedDot()
            }
            addView(item)
        }

        animateSelectedTabIndicator()
    }

    private fun animateSelectedTabIndicator() {
        val progressBar = getSelectedTabIndicator()
        if (progressBar == null) return

        expandProgressBarWidthAnimatorSet?.cancel()
        expandProgressBarWidthAnimator?.cancel()

        expandProgressBarWidthAnimatorSet = AnimatorSet()
        expandProgressBarWidthAnimator = ValueAnimator.ofInt(6, 28)
            .setDuration(600)

        expandProgressBarWidthAnimator?.addUpdateListener { animation ->
            val value = animation.animatedValue as Int
            // println("Animation. Animate progressbar width $value")

            progressBar.layoutParams?.width = value.toPx()
            progressBar.requestLayout()

            if (value == 28) {
                animateProgress(progressBar)
            }
        }

        expandProgressBarWidthAnimatorSet?.interpolator = UnifyMotion.EASE_IN_OUT
        expandProgressBarWidthAnimatorSet?.play(expandProgressBarWidthAnimator)
        expandProgressBarWidthAnimatorSet?.start()
    }

    private fun animateProgress(progressBar: ProgressBar) {
        progressAnimator?.cancel()
        progressAnimatorSet?.cancel()

        progressAnimator = ValueAnimator
            .ofInt(0, 100)
            .setDuration(6000)

        progressAnimatorSet = AnimatorSet()

        progressAnimator?.addUpdateListener { animation ->
            val value = animation.animatedValue as Int
            // println("Animation. Animate progress $value")
            progressBar.progress = value
            if (value == 100) {
                onProgressFinish(selectedPosition)
            }
        }

        progressAnimatorSet?.interpolator = LinearInterpolator()
        progressAnimatorSet?.play(progressAnimator)
        progressAnimatorSet?.start()
    }

    @SuppressLint("UnifyComponentUsage")
    private fun createSelectedDot(): ProgressBar {
        val progressBar = ProgressBar(
            context,
            null,
            android.R.attr.progressBarStyleHorizontal
        ).apply {
            layoutParams = LayoutParams(
                DOT_INDICATOR_ACTIVE_WIDTH.toPx(),
                DOT_INDICATOR_ACTIVE_HEIGHT.toPx()
            ).apply {
                leftMargin = DOT_INDICATOR_MARGIN_START.toPx()
            }
            max = 100
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
            DOT_INDICATOR_INACTIVE_WIDTH.toPx(), DOT_INDICATOR_INACTIVE_HEIGHT.toPx()
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

    private fun startAnimation() {
        expandProgressBarWidthAnimatorSet?.start()
        progressAnimatorSet?.start()
        progressAnimator?.start()
    }

    private fun resumeAnimation() {
        expandProgressBarWidthAnimatorSet?.resume()
        progressAnimatorSet?.resume()
        progressAnimator?.resume()
    }

    private fun pauseAnimation() {
        expandProgressBarWidthAnimatorSet?.pause()
        progressAnimatorSet?.pause()
        progressAnimator?.pause()
    }

    private fun cancelAnimation() {
        expandProgressBarWidthAnimatorSet?.cancel()
        progressAnimatorSet?.cancel()
        progressAnimator?.cancel()
    }

    private fun getSelectedTabIndicator(): ProgressBar? {
        for (i in 0..childCount) {
            val indicator = getChildAt(i)
            if (indicator is ProgressBar) {
                return indicator
            }
        }

        return null
    }
}

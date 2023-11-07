package com.tokopedia.shop.info.view.custom

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.os.CountDownTimer
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.shop.R
import com.tokopedia.unifycomponents.toPx

class ProgressibleTabLayoutView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), DefaultLifecycleObserver {

    private var countDownTimer: CountDownTimer? = null
    private var progressAnimator: ObjectAnimator? = null

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

    fun renderTabIndicator(
        config: Config,
        lifecycle: Lifecycle,
        selectedTabIndicatorIndex: Int,
        onTimerFinish: () -> Unit
    ) {
        lifecycle.addObserver(this)

        println("Timer. Executing renderTabIndicator")
        cancelTimer()
        println("Timer. cancelling timer")

        // Remove current tab indicators
        removeAllViews()

        for (currentIndex in 0 until config.itemCount) {
            val item = if (currentIndex == selectedTabIndicatorIndex) {
                println("Timer. Creating selected dot on $currentIndex")
                createSelectedDot(config.totalDuration)
            } else {
                println("Timer. Creating unselected index on $currentIndex")
                createUnselectedDot()
            }
            addView(item)
        }

        setupCountDownTimer(
            totalDuration = config.totalDuration,
            intervalDuration = config.intervalDuration,
            onTimerFinish = onTimerFinish
        )

        startTimer()
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

    private fun setupCountDownTimer(
        totalDuration: Long,
        intervalDuration: Long,
        onTimerFinish: () -> Unit
    ) {
        println("Timer. setupCountDownTimer")
        val progressBar = getSelectedTabIndicator()

        progressAnimator = ObjectAnimator.ofInt(progressBar, "progress", 0)
        progressAnimator?.duration = totalDuration

        println("Timer. setupCountDownTimer progressBar is $progressBar")
        countDownTimer = object : CountDownTimer(totalDuration, intervalDuration) {
            override fun onTick(millisUntilFinished: Long) {
                val completedMillis = totalDuration - millisUntilFinished

                progressAnimator?.setIntValues(completedMillis.toInt())
                println("Timer. setupCountDownTimer onTick completed millis $completedMillis, millisuntilfinished $millisUntilFinished")
            }

            override fun onFinish() {
                progressAnimator?.setIntValues(totalDuration.toInt())

                onTimerFinish()

                println("Timer. setupCountDownTimer onFinish")
            }
        }
    }

    private fun startTimer() {
        println("Timer. startTimer")
        cancelTimer()
        countDownTimer?.start()
        progressAnimator?.setIntValues(Int.ZERO)
        progressAnimator?.start()
    }

    private fun cancelTimer() {
        println("Timer. cancelTimer")
        countDownTimer?.cancel()
        progressAnimator?.cancel()
    }

    @SuppressLint("UnifyComponentUsage")
    private fun createSelectedDot(totalDuration: Long): ProgressBar {
        val progressBar =
            ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal).apply {
                layoutParams = LayoutParams(
                    DOT_INDICATOR_ACTIVE_WIDTH.toPx(),
                    DOT_INDICATOR_ACTIVE_HEIGHT.toPx()
                ).apply {
                    leftMargin = DOT_INDICATOR_MARGIN_START.toPx()
                }
                max = totalDuration.toInt()
                progress = Int.ZERO
            }

        val backgroundDrawable = ContextCompat.getDrawable(
            context,
            R.drawable.shape_shop_review_rounded_progressbar_background
        )
        val progressDrawable = ContextCompat.getDrawable(
            context,
            R.drawable.shape_shop_review_rounded_progressbar_progress
        )

        progressBar.progressDrawable = progressDrawable
        progressBar.background = backgroundDrawable

        return progressBar
    }

    private fun createUnselectedDot(): ImageView {
        val imageView = ImageView(context)
        val params =
            LayoutParams(DOT_INDICATOR_INACTIVE_WIDTH.toPx(), DOT_INDICATOR_INACTIVE_HEIGHT.toPx())
        params.leftMargin = DOT_INDICATOR_MARGIN_START.toPx()
        imageView.layoutParams = params

        imageView.setImageDrawable(
            ContextCompat.getDrawable(
                context,
                R.drawable.shop_info_review_dot_indicator_unselected
            )
        )

        return imageView
    }

    override fun onResume(owner: LifecycleOwner) {
        println("Timer. onResume")
        super.onResume(owner)
        startTimer()
    }

    override fun onPause(owner: LifecycleOwner) {
        println("Timer. onPause")
        super.onPause(owner)
        cancelTimer()
    }

    override fun onStop(owner: LifecycleOwner) {
        println("Timer. onStop")
        super.onStop(owner)
        cancelTimer()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        println("Timer. onDestroy")
        super.onDestroy(owner)
        cancelTimer()
    }
}

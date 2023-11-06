package com.tokopedia.shop.info.view.custom

import android.annotation.SuppressLint
import android.content.Context
import android.os.CountDownTimer
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.shop.R
import com.tokopedia.unifycomponents.toPx

class ProgressibleTabLayoutView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var countDownTimer: CountDownTimer? = null

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
        
        private const val ONE_HUNDRED = 100
    }

    fun renderTabIndicator(config: Config, selectedIndex: Int, onTimerFinish: () -> Unit) {
        //Remove current tab indicators
        removeAllViews()

        for (currentIndex in 0..config.itemCount) {
            val item = if (currentIndex == selectedIndex) {
                createSelectedDot()
            } else {
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
        val progressBar = getSelectedTabIndicator()

        countDownTimer = object : CountDownTimer(totalDuration, intervalDuration) {
            override fun onTick(millisUntilFinished: Long) {
                val remainingProgressPercent = (millisUntilFinished.toFloat() / totalDuration.toFloat()) * ONE_HUNDRED.toFloat()
                val progressPercent = ONE_HUNDRED.toFloat() - remainingProgressPercent

                progressBar?.progress = progressPercent.toInt()
            }

            override fun onFinish() {
                progressBar?.progress = ONE_HUNDRED

                onTimerFinish()
            }
        }
    }


    private fun startTimer() {
        countDownTimer?.cancel()
        countDownTimer?.start()
    }

    @SuppressLint("UnifyComponentUsage")
    private fun createSelectedDot(): ProgressBar {
        val progressBar =
            ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal).apply {
                layoutParams = LayoutParams(
                    DOT_INDICATOR_ACTIVE_WIDTH.toPx(),
                    DOT_INDICATOR_ACTIVE_HEIGHT.toPx()
                ).apply {
                    leftMargin = DOT_INDICATOR_MARGIN_START.toPx()
                }
                max = ONE_HUNDRED
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

}

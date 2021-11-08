package com.tokopedia.play.broadcaster.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.util.extension.millisToMinutes
import com.tokopedia.play.broadcaster.util.extension.millisToRemainingSeconds
import com.tokopedia.unifyprinciples.Typography
import java.util.concurrent.TimeUnit


/**
 * Created by mzennis on 11/06/20.
 */
class PlayTimerView : ConstraintLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private var tvTimeCounter: Typography
    private var tvTimeCounterEnd: AppCompatTextView

    init {
        val view = View.inflate(context, R.layout.view_play_timer, this)
        tvTimeCounter = view.findViewById(R.id.tv_time_counter)
        tvTimeCounterEnd = view.findViewById(R.id.tv_time_counter_end)
    }

    fun showCounter(timeInMillis: Long) {
        val noticePeriod = getNoticePeriod(timeInMillis)
        if (noticePeriod != null) {
            tvTimeCounter.hide()
            tvTimeCounterEnd.show()
        } else {
            tvTimeCounter.show()
            tvTimeCounterEnd.hide()
        }
        tvTimeCounter.text = context.getString(
            R.string.play_live_broadcast_remaining_duration_format,
            timeInMillis.millisToMinutes(),
            timeInMillis.millisToRemainingSeconds()
        )
        tvTimeCounterEnd.text = context.getString(R.string.play_live_broadcast_time_left, noticePeriod)
    }

    private fun getNoticePeriod(timeInMillis: Long): Long? {
        val noticePeriodList = listOf(
            Triple(NOTICE_PERIOD_FIVE_MINUTES, getMinNoticePeriod(NOTICE_PERIOD_FIVE_MINUTES), getMaxNoticePeriod(NOTICE_PERIOD_FIVE_MINUTES)),
            Triple(NOTICE_PERIOD_TWO_MINUTES, getMinNoticePeriod(NOTICE_PERIOD_TWO_MINUTES), getMaxNoticePeriod(NOTICE_PERIOD_TWO_MINUTES)),
        )
        val noticePeriod = noticePeriodList.firstOrNull { timeInMillis in it.second..it.third }
        return noticePeriod?.first
    }

    private fun getMinNoticePeriod(minute: Long) = TimeUnit.MINUTES.toMillis(minute)-4000

    private fun getMaxNoticePeriod(minute: Long) = TimeUnit.MINUTES.toMillis(minute)+1000

    companion object {
        private const val NOTICE_PERIOD_FIVE_MINUTES = 5L
        private const val NOTICE_PERIOD_TWO_MINUTES = 2L
    }
}
package com.tokopedia.play.broadcaster.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.play.broadcaster.R
import com.tokopedia.unifyprinciples.Typography


/**
 * Created by mzennis on 11/06/20.
 */
class PlayTimerView : ConstraintLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    private var tvTimeCounter: Typography
    private var tvTimeCounterEnd: AppCompatTextView

    init {
        val view = View.inflate(context, R.layout.view_play_timer, this)
        tvTimeCounter = view.findViewById(R.id.tv_time_counter)
        tvTimeCounterEnd = view.findViewById(R.id.tv_time_counter_end)
    }

    fun showTimeRemaining(minutesUntilFinished: Long = 0L) {
        setVisibilityWhenTimesRunOut(true)
        tvTimeCounterEnd.text = context.getString(R.string.play_live_broadcast_time_left, minutesUntilFinished)
    }

    fun showCounterDuration(timeLeft: String = "") {
        setVisibilityWhenTimesRunOut(false)
        tvTimeCounter.text = timeLeft
    }

    private fun setVisibilityWhenTimesRunOut(state: Boolean) {
        tvTimeCounter.visibility = if (state) View.INVISIBLE else View.VISIBLE
        tvTimeCounterEnd.visibility = if (state) View.VISIBLE else View.INVISIBLE
    }
}
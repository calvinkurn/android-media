package com.tokopedia.play.view.viewcomponent

import android.view.ViewGroup
import android.widget.FrameLayout
import com.tokopedia.play.R
import com.tokopedia.play_common.viewcomponent.ViewComponent
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
import java.util.*

/**
 * Created by jegul on 28/06/21
 */
class EngagementToolsTapViewComponent(
        container: ViewGroup,
        listener: Listener
) : ViewComponent(container, R.id.view_engagement_tap) {

    private val flEngagementTap = findViewById<FrameLayout>(R.id.fl_engagement_tap)
    private val timerTap = findViewById<TimerUnifySingle>(R.id.timer_tap)

    init {
        flEngagementTap.setOnClickListener {

        }
    }

    fun setTimer(durationInMs: Long, onFinished: () -> Unit) {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MILLISECOND, durationInMs.toInt())

        timerTap.pause()
        timerTap.targetDate = calendar
        timerTap.onFinish = onFinished
        timerTap.resume()
    }

    override fun hide() {
        super.hide()
        timerTap.pause()
    }

    interface Listener {

        fun onTapClicked(view: EngagementToolsTapViewComponent)
    }
}
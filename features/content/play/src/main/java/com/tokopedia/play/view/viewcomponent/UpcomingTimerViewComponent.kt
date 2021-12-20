package com.tokopedia.play.view.viewcomponent

import android.view.ViewGroup
import androidx.annotation.IdRes
import com.tokopedia.play.R
import com.tokopedia.play_common.util.datetime.PlayDateTimeFormatter
import com.tokopedia.play_common.viewcomponent.ViewComponent
import com.tokopedia.unifycomponents.timer.TimerUnifyHighlight
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

/**
 * Created By : Jonathan Darwin on September 06, 2021
 */
class UpcomingTimerViewComponent(
    container: ViewGroup,
    @IdRes idRes: Int,
    private val listener: Listener,
): ViewComponent(container, idRes) {

    private val timerUpcoming = findViewById<TimerUnifyHighlight>(R.id.timer_upcoming)

    fun setupTimer(startTime: String) {
        val targetCalendar = PlayDateTimeFormatter.convertToCalendar(startTime)
        targetCalendar?.let { target ->
            if(target.timeInMillis > Calendar.getInstance().timeInMillis)
                timerUpcoming.targetDate = target
            show()
        } ?: hide()

        timerUpcoming.onFinish = { listener.onTimerFinish(this) }
    }

    fun stopTimer() {
        timerUpcoming.pause()
        hide()
    }

    interface Listener {
        fun onTimerFinish(view: UpcomingTimerViewComponent)
    }
}
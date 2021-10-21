package com.tokopedia.play.view.viewcomponent

import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.appcompat.widget.AppCompatTextView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.R
import com.tokopedia.play_common.util.datetime.PlayDateTimeFormatter
import com.tokopedia.play_common.viewcomponent.ViewComponent
import com.tokopedia.unifycomponents.timer.TimerUnifyHighlight
import java.util.*

/**
 * Created By : Jonathan Darwin on September 06, 2021
 */
class UpcomingTimerViewComponent(
    container: ViewGroup,
    @IdRes idRes: Int
): ViewComponent(container, idRes) {

    private val timerUpcoming = findViewById<TimerUnifyHighlight>(R.id.timer_upcoming)

    fun setupTimer(startTime: String) {
        val targetCalendar = PlayDateTimeFormatter.convertToCalendar(startTime)
        targetCalendar?.let { target ->
            if(target.timeInMillis > Calendar.getInstance().timeInMillis)
                timerUpcoming.targetDate = target
        } ?: hide()
    }

    fun stopTimer() {
        timerUpcoming.pause()
        hide()
    }
}
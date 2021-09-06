package com.tokopedia.play.view.viewcomponent

import android.view.ViewGroup
import androidx.annotation.IdRes
import com.tokopedia.play.R
import com.tokopedia.play_common.viewcomponent.ViewComponent
import com.tokopedia.unifycomponents.timer.TimerUnifyHighlight
import java.util.*

/**
 * Created By : Jonathan Darwin on September 06, 2021
 */
class UpcomingTimerViewComponent(
    private val container: ViewGroup,
    @IdRes idRes: Int
): ViewComponent(container, idRes) {

    private val timerUpcoming = findViewById<TimerUnifyHighlight>(R.id.timer_upcoming)

    fun setTimer(calendar: Calendar) {
        timerUpcoming.targetDate = calendar
    }
}
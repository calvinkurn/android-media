package com.tokopedia.play.view.viewcomponent

import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.appcompat.widget.AppCompatTextView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.R
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
    private val tvTimerLabel = findViewById<AppCompatTextView>(R.id.tv_timer_label)
    private val tvUpcomingDate = findViewById<AppCompatTextView>(R.id.tv_upcoming_date)

    fun setTimer(calendar: Calendar) {
        timerUpcoming.show()
        tvTimerLabel.show()
        tvUpcomingDate.hide()
        timerUpcoming.targetDate = calendar
    }

    fun setDate(date: String) {
        tvUpcomingDate.show()
        timerUpcoming.hide()
        tvTimerLabel.hide()
        tvUpcomingDate.text = date
    }
}
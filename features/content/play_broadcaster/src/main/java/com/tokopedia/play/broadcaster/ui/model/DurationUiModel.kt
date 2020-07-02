package com.tokopedia.play.broadcaster.ui.model

import com.tokopedia.play.broadcaster.util.durationFormatted
import com.tokopedia.play.broadcaster.util.minuteToSecond


/**
 * Created by mzennis on 03/07/20.
 */
data class DurationUiModel(
        val duration: Long,
        val maxDuration: Long,
        val remaining: Long
) {

    private val timeoutList: List<Timeout> = default()

    fun handleLiveDuration(onActive: (String) -> Unit, onAlmostFinish: (Long) -> Unit, onFinish: () -> Unit) {
        if (remaining == 0L) {
            onFinish()
            return
        }

        val timeout = timeoutList.firstOrNull { remaining in it.minSecond..it.maxSecond }
        if (timeout == null)
            onActive(duration.durationFormatted())
        else
            onAlmostFinish(timeout.minute)
    }

    private fun default(): List<Timeout> {
        return arrayListOf(
                Timeout(2),
                Timeout(5)
        )
    }

    data class Timeout(
            val minute: Long,
            var minSecond: Long = minute.minuteToSecond()-4,
            var maxSecond: Long = minute.minuteToSecond()+1
    )
}
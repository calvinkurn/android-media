package com.tokopedia.play.broadcaster.util.timer


/**
 * Created by mzennis on 17/03/21.
 */
data class CountDownTimeout(
        val minute: Long,
        val minMillis: Long = minuteToMillis(minute)-4000,
        val maxMillis: Long = minuteToMillis(minute)+1000
) {
    companion object {
        fun minuteToMillis(minute: Long) = (minute*1000*60)
    }
}
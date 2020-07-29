package com.tokopedia.play.broadcaster.pusher.timer


/**
 * Created by mzennis on 10/06/20.
 */
class Timeout(
        val minute: Long,
        var minMillis: Long = minuteToMillis(minute)-4000,
        var maxMillis: Long = minuteToMillis(minute)+1000
) {
    companion object {

        fun Default(): List<Timeout> {
            return arrayListOf(
                    Timeout(2),
                    Timeout(5)
            )
        }

        private fun minuteToMillis(minute: Long): Long = (minute*1000*60)
    }
}
package com.tokopedia.vouchercreation.common.utils

import android.os.CountDownTimer

class TimerRunnable(remainingTime: Long,
                    val onRemainingTimeTick: (String) -> Unit) : CountDownTimer(remainingTime, SECOND.toLong()) {

    companion object {
        private const val SECOND = 1000
        private const val MINUTE = SECOND * 60
        private const val HOUR = MINUTE * 60
        private const val DAY = HOUR * 24

        private const val TIMER_FORMAT = "%d hari : %dj : %dm : %dd"
    }

    override fun onFinish() {

    }

    override fun onTick(millisUntilFinished: Long) {
        onRemainingTimeTick(convertToTick(millisUntilFinished))
    }

    private fun convertToTick(remainingTime: Long): String {
        val days = remainingTime / DAY
        val hours = (remainingTime % DAY) / HOUR
        val minutes = (remainingTime % HOUR) / MINUTE
        val seconds = (remainingTime % MINUTE) / SECOND

        return String.format(TIMER_FORMAT, days.toInt(), hours.toInt(), minutes.toInt(), seconds.toInt())
    }
}
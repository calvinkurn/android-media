package com.tokopedia.vouchercreation.common.utils

import android.os.CountDownTimer
import java.util.*
import java.util.concurrent.TimeUnit

class Timer(private val onTick : (String) -> Unit) {

    companion object {
        private const val SECOND = 1000
        private const val TIMER_FORMAT = "%dj : %dm : %dd"
    }

    fun startCountdown(endDate: Date) {
        val dateDifferenceInMillis = findDateDifferenceInMillis(endDate)
        val timer = object : CountDownTimer(dateDifferenceInMillis, SECOND.toLong()) {
            override fun onFinish() {

            }

            override fun onTick(millisUntilFinished: Long) {
                val hours = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
                val minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
                val seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))
                val formattedTime = String.format(TIMER_FORMAT, hours.toInt(), minutes.toInt(), seconds.toInt())
                onTick(formattedTime)
            }
        }
        timer.start()
    }

    private fun findDateDifferenceInMillis(endDate: Date) : Long {
        val now = Calendar.getInstance()
        val endDateCalendar = Calendar.getInstance()
        endDateCalendar.time = endDate

        return endDateCalendar.timeInMillis - now.timeInMillis
    }


}
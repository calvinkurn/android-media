package com.tokopedia.vouchercreation.common.utils

import android.os.CountDownTimer
import java.util.*
import java.util.concurrent.TimeUnit

class Timer(private val endDate: Date) {

    private lateinit var timer : CountDownTimer
    private var onTicked : (String) -> Unit = {}

    companion object {
        private const val SECOND = 1000
        private const val TIMER_FORMAT = "%02d : %02d : %02d"
    }

    fun startCountdown() {
        val dateDifferenceInMillis = findDateDifferenceInMillis(endDate)
        timer = object : CountDownTimer(dateDifferenceInMillis, SECOND.toLong()) {
            override fun onFinish() {

            }

            override fun onTick(millisUntilFinished: Long) {
                val hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished)
                val minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished))
                val seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))

                val formattedTime = String.format(TIMER_FORMAT, hours.toInt(), minutes.toInt(), seconds.toInt())
                onTicked(formattedTime)
            }
        }
        timer.start()
    }

    fun setOnTickListener(onTicked : (String) -> Unit) {
        this.onTicked = onTicked
    }

    fun stopCountdown() {
        timer.cancel()
    }

    private fun findDateDifferenceInMillis(endDate: Date) : Long {
        val now = Calendar.getInstance()
        val endDateCalendar = Calendar.getInstance()
        endDateCalendar.time = endDate

        return endDateCalendar.timeInMillis - now.timeInMillis
    }


}
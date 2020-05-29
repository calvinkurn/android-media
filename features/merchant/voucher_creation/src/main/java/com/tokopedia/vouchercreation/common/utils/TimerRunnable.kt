package com.tokopedia.vouchercreation.common.utils

import android.content.Context
import android.os.CountDownTimer
import com.tokopedia.vouchercreation.R

class TimerRunnable(remainingTime: Long,
                    val onRemainingTimeTick: (String) -> Unit,
                    val context: Context) : CountDownTimer(remainingTime, 1000) {

    companion object {
        private const val SECOND = 1000
        private const val MINUTE = SECOND * 60
        private const val HOUR = MINUTE * 60
        private const val DAY = HOUR * 24
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

        return String.format(context.getString(R.string.mvc_detail_remaining_time), days.toInt().toString(), hours.toInt().toString(), minutes.toInt().toString(), seconds.toInt().toString())
    }
}
package com.tokopedia.promocheckout.widget

import android.os.CountDownTimer

class TimerPromoCheckout{
    private var countDownTimer: CountDownTimer? = null
    var listener: Listener? = null
    var expiredTimer : Long = 0
        set(value) {
            field = value
            setTimer()
        }

    interface Listener {
        fun onTick(l: Long)
        fun onFinishTick()
    }


    fun formatMilliSecondsToTime(milliseconds: Long): String {
        val seconds = (milliseconds / 1000).toInt() % 60
        val minutes = (milliseconds / (1000 * 60) % 60).toInt()
        val hours = (milliseconds / (1000 * 60 * 60)).toInt()
        return (twoDigitString(hours.toLong()) + " : " + twoDigitString(minutes.toLong()) + " : "
                + twoDigitString(seconds.toLong()))
    }

    private fun twoDigitString(number: Long): String {

        if (number == 0L) {
            return "00"
        }

        return if (number / 10 == 0L) {
            "0$number"
        } else number.toString()

    }

    private fun setTimer() {
        countDownTimer = object : CountDownTimer(expiredTimer * 1000, 100) {
            override fun onTick(l: Long) {
                listener?.onTick(l/1000)
            }
            override fun onFinish() {
                listener?.onFinishTick()
            }
        }
    }

    fun start() {
        countDownTimer?.start()
    }

    fun cancel() {
        countDownTimer?.cancel()
    }

    companion object {
        val COUPON_SHOW_COUNTDOWN_MAX_LIMIT_ONE_DAY : Long = 86400
    }
}
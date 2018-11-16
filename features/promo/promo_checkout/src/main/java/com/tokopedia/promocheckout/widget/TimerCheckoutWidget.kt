package com.tokopedia.promocheckout.widget

import android.content.Context
import android.os.CountDownTimer
import android.util.AttributeSet
import android.view.View
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.promocheckout.R
import kotlinx.android.synthetic.main.timer_checkout_widget.view.*

class TimerCheckoutWidget @JvmOverloads constructor(
        context: Context, val attrs: AttributeSet? = null, val defStyleAttr: Int = 0
) : BaseCustomView(context, attrs, defStyleAttr) {

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

    init {
        View.inflate(context, R.layout.timer_checkout_widget, this)
        progressBar.max = 100
        val styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.TimerCheckoutWidget)
        styledAttributes.recycle()
    }

    private fun formatMilliSecondsToTime(milliseconds: Long): String {
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
                textTimer?.text = formatMilliSecondsToTime(l)
                val percent = (expiredTimer * 100 / COUPON_SHOW_COUNTDOWN_MAX_LIMIT_ONE_DAY).toInt()
                progressBar?.progress = Math.abs(percent)
            }

            override fun onFinish() {
                    listener?.onFinishTick()
            }
        }
        invalidate()
        requestLayout()
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

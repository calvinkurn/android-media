package com.tokopedia.thankyou_native.presentation.views

import android.content.Context
import android.os.CountDownTimer
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatTextView
import com.tokopedia.thankyou_native.R
import java.lang.ref.WeakReference
import java.util.*

class ThankYouPageTimerView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : FrameLayout(context, attrs, defStyleAttr) {


    private val HOUR = 3600000
    private val MIN = 60000
    private val SEC = 1000

    private val TIMER_FINISHED_STR = "00:00:00"

    var timer: CountDownTimer? = null
    private var timerRunning: Boolean = false

    private var expireUnixTime: Long = 0
    private var currentDuration: Long = 0
    private val layout = R.layout.thank_timer_view
    private var mTvTimer: AppCompatTextView? = null

    private var weakListener: WeakReference<ThankTimerViewListener>? = null

    init {
        initUI()
    }

    private fun initUI() {
        val v = LayoutInflater.from(context).inflate(layout, null)
        addView(v)
        mTvTimer = v.findViewById(R.id.tvTimer)
    }

    fun setExpireTimeUnix(expireOnTimeUnix: Long, listener: ThankTimerViewListener) {
        if (timerRunning) {
            return
        }
        weakListener = WeakReference(listener)
        expireUnixTime = expireOnTimeUnix * 1000L
        updateText(getCurrentDuration())
    }

    private fun getCurrentDuration(): Long {
        return expireUnixTime - System.currentTimeMillis()
    }

    fun startTimer() {
        currentDuration = getCurrentDuration()
        if (timerRunning || currentDuration <= 0) {
            updateText(0L)
            return
        }
        timerRunning = true

        timer = object : CountDownTimer(currentDuration, 1000) {
            override fun onTick(millis: Long) {
                currentDuration = millis
                updateText(millis)
            }

            override fun onFinish() {
                updateText(0L)
                stopTimer()
            }
        }
        timer?.start()
    }

    fun stopTimer() {
        if (timerRunning) {
            timerRunning = false
            timer?.cancel()
        }
    }

    private fun updateText(duration: Long) {
        val text = generateCountdownText(duration)
        mTvTimer?.let { timerView ->
            timerView.text = text
        }

    }

    private fun generateCountdownText(duration: Long): String? {
        if (duration <= 0) {
            notifyListener()
            return TIMER_FINISHED_STR
        }
        val hr = (duration / HOUR).toInt()
        val min = ((duration - hr * HOUR) / MIN).toInt()
        val sec = ((duration - hr * HOUR - min * MIN) / SEC).toInt()
        val locale: Locale = Locale.getDefault()
        val format = "%02d"
        val formattedHr: String = String.format(locale, format, hr)
        val formattedMin: String = String.format(locale, format, min)
        val formattedSec: String = String.format(locale, format, sec)
        return String.format(locale, "%s:%s:%s", formattedHr, formattedMin, formattedSec)
    }

    private fun notifyListener() {
        weakListener?.get()?.onTimerFinished()
        weakListener?.clear()
    }

    interface ThankTimerViewListener {
        fun onTimerFinished()
    }

}
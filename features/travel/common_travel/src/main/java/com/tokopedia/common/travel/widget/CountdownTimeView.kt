package com.tokopedia.common.travel.widget

import android.content.Context
import android.os.CountDownTimer
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.StyleRes
import androidx.appcompat.widget.AppCompatTextView
import com.tokopedia.common.travel.R
import com.tokopedia.unifycomponents.BaseCustomView
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * @author by alvarisi on 11/23/17.
 */
class CountdownTimeView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    private var expiredDate: Date? = null
    private var timeTextView: AppCompatTextView? = null
    private var containerLayout: LinearLayout? = null
    private var listener: OnActionListener? = null
    private var hoursLabel: String? = null
    private var minutesLabel: String? = null
    private var secondsLabel: String? = null
    private var countDownTimer: CountDownTimer? = null

    interface OnActionListener {
        fun onFinished()
    }

    init {
        init(attrs)
    }

    private fun init() {
        val view = inflate(context, R.layout.widget_countdown_time, this)
        timeTextView = view.findViewById<View>(R.id.tv_time) as AppCompatTextView
        containerLayout = view.findViewById<View>(R.id.container) as LinearLayout
    }

    private fun init(attrs: AttributeSet?) {
        init()
        attrs?.let {
            val styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.CountdownTimeView)
            try {
                hoursLabel = styledAttributes.getString(R.styleable.CountdownTimeView_ctv_hour_title)
                minutesLabel = styledAttributes.getString(R.styleable.CountdownTimeView_ctv_minute_title)
                secondsLabel = styledAttributes.getString(R.styleable.CountdownTimeView_ctv_second_title)
            } finally {
                styledAttributes.recycle()
            }
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        if (TextUtils.isEmpty(hoursLabel)) {
            hoursLabel = DEFAULT_HOURS
        }
        if (TextUtils.isEmpty(minutesLabel)) {
            minutesLabel = DEFAULT_MINUTE
        }
        if (TextUtils.isEmpty(secondsLabel)) {
            secondsLabel = DEFAULT_SECOND
        }
    }

    fun setListener(listener: OnActionListener?) {
        this.listener = listener
    }

    fun setExpiredDate(date: Date?) {
        expiredDate = date
    }

    fun start() {
        expiredDate?.let {
            val now: Calendar = GregorianCalendar(TimeZone.getTimeZone("Asia/Jakarta"))
            val delta = it.time - now.timeInMillis
            if (TimeUnit.MILLISECONDS.toDays(delta) < 1) {
                timeTextView?.text = getCountdownText(delta)
                containerLayout?.visibility = VISIBLE
                countDownTimer = object : CountDownTimer(delta, 1000) {
                    override fun onTick(millisUntilFinished: Long) {
                        timeTextView?.text = getCountdownText(millisUntilFinished)
                    }

                    override fun onFinish() {
                        listener?.onFinished()
                        containerLayout!!.visibility = GONE
                    }
                }
                countDownTimer?.start()
            }
        }
    }

    fun cancel() {
        countDownTimer?.cancel()
    }

    private fun getCountdownText(millisUntilFinished: Long): String {
        var countdown = ""
        countdown = if (millisUntilFinished / (1000 * 60 * 60) % 24 > 0) {
            (millisUntilFinished / (1000 * 60 * 60) % 24).toString() + " " + hoursLabel + millisUntilFinished / (1000 * 60) % 60 + " " + minutesLabel + " " + millisUntilFinished / 1000 % 60 + " " + secondsLabel
        } else {
            (millisUntilFinished / (1000 * 60) % 60).toString() + " " + minutesLabel + " " + millisUntilFinished / 1000 % 60 + " " + secondsLabel
        }
        return countdown
    }

    fun setStyle(@StyleRes style: Int) {
        timeTextView?.setTextAppearance(context, style)
    }

    companion object {
        private const val DEFAULT_HOURS = "h"
        private const val DEFAULT_MINUTE = "m"
        private const val DEFAULT_SECOND = "s"
    }
}
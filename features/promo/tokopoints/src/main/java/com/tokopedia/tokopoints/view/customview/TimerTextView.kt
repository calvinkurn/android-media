package com.tokopedia.tokopoints.view.customview

import android.content.Context
import android.graphics.Typeface
import android.os.CountDownTimer
import android.text.Html
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.style.StyleSpan
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.tokopedia.tokopoints.R
import com.tokopedia.tokopoints.view.model.rewrdsStatusMatching.MetadataItem
import com.tokopedia.tokopoints.view.tokopointhome.header.TopSectionVH
import com.tokopedia.tokopoints.view.util.CommonConstant.TimerConstant.Companion.DAY_TEXT
import com.tokopedia.tokopoints.view.util.CommonConstant.TimerConstant.Companion.HOUR_TEXT
import com.tokopedia.tokopoints.view.util.CommonConstant.TimerConstant.Companion.MINUTE_TEXT
import com.tokopedia.tokopoints.view.util.CommonConstant.TimerConstant.Companion.SECOND_TEXT
import com.tokopedia.tokopoints.view.util.CommonConstant.TimerConstant.Companion.TIMER_DESC
import com.tokopedia.unifyprinciples.Typography

class TimerTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {

    private var timerValue: Typography? = null
    private var countDownTimer: CountDownTimer? = null
    private var mStatusTimerListener: StatusTimerListener? = null

    init {
        setUpUI()
    }

    private fun setUpUI() {
        val view = View.inflate(context, R.layout.tp_layout_timer_ticker, null)
        timerValue = view.findViewById(R.id.timer_value)
        addView(view)
    }

    fun setTimer(data: MetadataItem?) {
        data?.isShowTime = true
        data?.timeRemainingSeconds = 61
        val statusLinking = data?.text?.content
        if (data?.isShowTime == true && data.timeRemainingSeconds != null && data.timeRemainingSeconds!! > 0) {
            countDownTimer?.cancel()
            countDownTimer = object : CountDownTimer(data.timeRemainingSeconds!! * 1000, 1000) {

                override fun onTick(l: Long) {
                    data.timeRemainingSeconds = l / 1000
                    val seconds = (l / 1000).toInt() % 60
                    val minutes = (l / (1000 * 60) % 60).toInt()
                    val hours = (l / (1000 * 60 * 60) % 24).toInt()
                    val days = (l / (1000 * 60 * 60 * 24)).toInt()

                    if (days >= 1) {
                        setTimerValue(statusLinking ?: "", DAY_TEXT, days.toString())
                    } else if (hours >= 1 && days < 1) {
                        setTimerValue(statusLinking ?: "", HOUR_TEXT, hours.toString())
                    } else if (minutes >= 1 && hours < 1) {
                        setTimerValue(statusLinking ?: "", MINUTE_TEXT, minutes.toString())
                    } else if (seconds >= 1 && minutes < 1) {
                        setTimerValue(statusLinking ?: "", SECOND_TEXT, seconds.toString())
                    }
                }

                override fun onFinish() {
                    mStatusTimerListener?.onTimerFinish()
                }
            }.start()
        } else {
            timerValue?.text = statusLinking ?: ""
        }
    }

    private fun setTimerValue(statusLinking: String, descString: String, timeValue: String) {
        val timerDesc = String.format(TIMER_DESC, descString)
        val timerString = "$timeValue $timerDesc"
   /*     val timerStr = SpannableStringBuilder(timerString)
        timerStr.setSpan(
            StyleSpan(Typeface.BOLD),
            0,
            timerString.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )*/
       val timerStr =  (Html.fromHtml("<b>$timerString</b>"))
        val correctedTimeString = "$statusLinking $timerStr"
        timerValue?.text = correctedTimeString
    }

    fun setStatusTimerListener(statusTimerListener: StatusTimerListener) {
        mStatusTimerListener = statusTimerListener
    }

    interface StatusTimerListener {
        fun onTimerFinish()
    }
}
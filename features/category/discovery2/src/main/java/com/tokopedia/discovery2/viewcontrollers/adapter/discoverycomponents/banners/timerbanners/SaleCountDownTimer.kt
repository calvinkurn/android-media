package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.banners.timerbanners

import android.os.CountDownTimer
import android.util.Log
import com.tokopedia.discovery2.data.multibannerresponse.timmerwithbanner.TimerDataModel
import kotlin.math.roundToInt

class SaleCountDownTimer(millisInFuture: Long, countDownInterval: Long = 1000,
                         private val showDays: Boolean = false,
                         private val isCurrentTimer: Boolean = true,
                         private val getTimerData: (timerDataModel: TimerDataModel) -> Unit) :
        CountDownTimer(millisInFuture, countDownInterval) {
    private val timeDiffModel = TimerDataModel()
    private var days = 0

    override fun onTick(millisUntilFinished: Long) {
        timeDiffModel.timeFinish = false
        if (isCurrentTimer) {
            var seconds = (millisUntilFinished / 1000f).roundToInt()
            var minutes = seconds / 60
            var hours = minutes / 60

            if (showDays) {
                days = hours / 24
                hours %= 24
            }
            minutes %= 60
            seconds %= 60
            if (showDays) timeDiffModel.days = days
            timeDiffModel.hours = hours
            timeDiffModel.minutes = minutes
            timeDiffModel.seconds = seconds
            Log.d("Days", "days: " + days + " hours: " + hours + " minute: " + minutes + " seconds: " + seconds)
            getTimerData.invoke(timeDiffModel)
        }
    }

    override fun onFinish() {
        timeDiffModel.days = 0
        timeDiffModel.hours = 0
        timeDiffModel.minutes = 0
        timeDiffModel.seconds = 0
        timeDiffModel.timeFinish = true
        getTimerData.invoke(timeDiffModel)
    }
}
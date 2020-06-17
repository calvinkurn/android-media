package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.banners.timerbanners

import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.data.multibannerresponse.timmerwithbanner.TimerDataModel

class SaleCountDownTimer(millisInFuture: Long, countDownInterval: Long = 1000) : CountDownTimer(millisInFuture, countDownInterval) {

    val mutableTimeDiffModel: MutableLiveData<TimerDataModel> = MutableLiveData()
    private val timeDiffModel = TimerDataModel()
    private val secondsInMilli: Long = 1000
    private val minutesInMilli = secondsInMilli * 60
    private val hoursInMilli = minutesInMilli * 60
    private val daysInMilli = hoursInMilli * 24

    override fun onTick(millisUntilFinished: Long) {
        var timeLeftInMillis = millisUntilFinished

        timeDiffModel.days = timeLeftInMillis / daysInMilli
        timeLeftInMillis %= daysInMilli

        timeDiffModel.hours = timeLeftInMillis / hoursInMilli
        timeLeftInMillis %= hoursInMilli

        timeDiffModel.minutes = timeLeftInMillis / minutesInMilli
        timeLeftInMillis %= minutesInMilli

        timeDiffModel.seconds = timeLeftInMillis / secondsInMilli
        mutableTimeDiffModel.value = timeDiffModel
    }

    override fun onFinish() {
        timeDiffModel.days = 0L
        timeDiffModel.hours = 0L
        timeDiffModel.minutes = 0L
        timeDiffModel.seconds = 0L
    }
}
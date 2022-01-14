package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.lihatsemua

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.Utils.Companion.TIMER_DATE_FORMAT
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import java.util.*

class LihatSemuaViewModel(application: Application, val component: ComponentsItem, val position: Int) : DiscoveryBaseViewModel() {
    private val itemData: MutableLiveData<ComponentsItem> = MutableLiveData()
    var timerWithBannerCounter: CountDownTimer? = null
    private val restartStoppedTimerEvent: SingleLiveEvent<Boolean> = SingleLiveEvent()
    private var timerSetToZero:Boolean = false
    private var isTimerStopped = false

    fun getComponentData(): LiveData<ComponentsItem> = itemData
    fun getRestartTimerAction(): LiveData<Boolean> = restartStoppedTimerEvent

    override fun onAttachToViewHolder() {
        super.onAttachToViewHolder()
        itemData.value = component
    }

    fun startTimer(timerUnify: TimerUnifySingle) {
        if(timerSetToZero){
            timerSetToZero = false
            return
        }
        val futureSaleTab = Utils.isFutureSale(getStartDate(),TIMER_DATE_FORMAT)
        val timerData: String? = if (futureSaleTab) getStartDate() else getEndDate()
        if (!timerData.isNullOrEmpty()) {
            val currentSystemTime = Calendar.getInstance().time
            val parsedEndDate = Utils.parseData(timerData,TIMER_DATE_FORMAT)
            parsedEndDate?.let { parsedDate ->
                val saleTimeMillis = parsedDate.time - currentSystemTime.time
                if (saleTimeMillis > 0) {
                    val parsedCalendar: Calendar = Calendar.getInstance()
                    parsedCalendar.time = parsedEndDate
                    timerUnify.targetDate = parsedCalendar
                    timerUnify.onTick = {
                        timerUnify.timer?.let { countDownTimer ->
                            if (countDownTimer != timerWithBannerCounter) {
                                timerWithBannerCounter = countDownTimer
                            }
                            timerUnify.post {
                                timerUnify.onTick = null
                            }
                        }
                    }
                } else {
                    timerSetToZero = true
                    timerUnify.targetDate = Calendar.getInstance().apply {
                        timeInMillis += 999
                    }
                }
            }
        }
    }

    fun getStartDate(): String {
        if (!component.data.isNullOrEmpty()) {
            return component.data?.firstOrNull()?.startDate ?: ""
        }
        return ""
    }

    fun getEndDate(): String {
        if (!component.data.isNullOrEmpty()) {
            return component.data?.firstOrNull()?.endDate ?: ""
        }
        return ""
    }

    override fun onStop() {
        stopTimer()
        isTimerStopped = true
        super.onStop()
    }

    override fun onDestroy() {
        stopTimer()
        super.onDestroy()
    }

    override fun onResume() {
        if (isTimerStopped) {
            restartStoppedTimerEvent.setValue(true)
            isTimerStopped = false
        }
        super.onResume()
    }

    override fun onDetachToViewHolder() {
        stopTimer()
    }

    fun stopTimer() {
        timerWithBannerCounter?.cancel()
        timerWithBannerCounter = null
    }
}
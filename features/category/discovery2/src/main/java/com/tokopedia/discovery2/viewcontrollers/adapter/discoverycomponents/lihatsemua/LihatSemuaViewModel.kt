package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.lihatsemua

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
import java.util.*

class LihatSemuaViewModel(val application: Application, val component: ComponentsItem, val position: Int) : DiscoveryBaseViewModel() {
    private val itemData: MutableLiveData<ComponentsItem> = MutableLiveData()
    var timerWithBannerCounter: CountDownTimer? = null

    fun getComponentData(): LiveData<ComponentsItem> = itemData

    override fun onAttachToViewHolder() {
        super.onAttachToViewHolder()
        itemData.value = component
    }

    fun startTimer(timerUnify: TimerUnifySingle) {
        val futureSaleTab = Utils.isFutureSale(getStartDate())
        val timerData: String? = if (futureSaleTab) getStartDate() else getEndDate()
        if (!timerData.isNullOrEmpty()) {
            val currentSystemTime = Calendar.getInstance().time
            val parsedEndDate = Utils.parseData(timerData)
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
//                    TODO:: setup timer to 0:00
                }
            }
        }
    }

    fun getStartDate(): String {
        if (!component.data.isNullOrEmpty()) {
            return component.data?.firstOrNull()?.startTime ?: ""
        }
        return ""
    }

    fun getEndDate(): String {
        if (!component.data.isNullOrEmpty()) {
            return component.data?.firstOrNull()?.endTime ?: ""
        }
        return ""
    }}
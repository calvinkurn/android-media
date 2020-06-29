package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.lihatflashsaletimerwidget

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.multibannerresponse.timmerwithbanner.TimerDataModel
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.banners.timerbanners.SaleCountDownTimer
import java.text.SimpleDateFormat
import java.util.*

class LihatFlashSaleTimerViewModel(val application: Application, componentData: ComponentsItem, val position: Int) : DiscoveryBaseViewModel() {

    private val saleWidgetData: MutableLiveData<ComponentsItem> = MutableLiveData()
    private var timeCounter: SaleCountDownTimer? = null
    private val elapsedTime: Long = 1000

    init {
        saleWidgetData.value = componentData
    }

    fun getComponentData(): LiveData<ComponentsItem> = saleWidgetData

    fun startTimer() {
        if (!parseFlashSaleDate().isEmpty()) {
            TimeZone.setDefault(TimeZone.getTimeZone(Utils.TIME_ZONE))
            val currentSystemTime = Calendar.getInstance().time
            val parsedEndDate = SimpleDateFormat(Utils.TIMER_DATE_FORMAT, Locale.getDefault())
                    .parse(parseFlashSaleDate())
            val saleTimeMillis = parsedEndDate.time - currentSystemTime.time

            if (saleTimeMillis > 0) {
                timeCounter = SaleCountDownTimer(saleTimeMillis, elapsedTime)
                timeCounter?.start()
            }
        }
    }

    private fun parseFlashSaleDate(): String {
        var flashSaleDate = ""
        if (!saleWidgetData.value?.data?.get(0)?.ongoingCampaignEndTime.isNullOrEmpty()) {
            val serverSaleDateTime = saleWidgetData.value?.data?.get(0)?.ongoingCampaignEndTime
            val date = serverSaleDateTime?.substring(0, 10)
            val time = serverSaleDateTime?.substring(11, 19)
            flashSaleDate = "${date}T${time}"
        }
        return flashSaleDate
    }

    fun stopTimer() {
        timeCounter?.cancel()
    }

    fun getTimerData(): LiveData<TimerDataModel> {
        return timeCounter?.mutableTimeDiffModel ?: MutableLiveData()
    }

    fun onLihatSemuaClicked(context: Context) {
        navigate(context, saleWidgetData.value?.data?.get(0)?.btnApplink)
    }
}
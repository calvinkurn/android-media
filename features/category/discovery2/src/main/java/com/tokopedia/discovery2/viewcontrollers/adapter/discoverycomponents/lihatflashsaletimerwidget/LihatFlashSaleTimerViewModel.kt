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
    private val mutableTimeDiffModel: MutableLiveData<TimerDataModel> = MutableLiveData()

    init {
        saleWidgetData.value = componentData
    }

    fun getComponentData(): LiveData<ComponentsItem> = saleWidgetData

    fun startTimer() {
        val parsedTimerDate = parseFlashSaleDate()
        if (parsedTimerDate.isNotEmpty()) {
            try {
                TimeZone.setDefault(TimeZone.getTimeZone(Utils.TIME_ZONE))
                val currentSystemTime = Calendar.getInstance().time
                SimpleDateFormat(Utils.TIMER_DATE_FORMAT, Locale.getDefault())
                        .parse(parsedTimerDate)?.let {
                            val saleTimeMillis = it.time - currentSystemTime.time
                            if (saleTimeMillis > 0) {
                                timeCounter = SaleCountDownTimer(saleTimeMillis, elapsedTime) { timerModel ->
                                    if (timerModel.timeFinish) stopTimer()
                                    mutableTimeDiffModel.value = timerModel
                                }
                                timeCounter?.start()
                            }
                        }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun parseFlashSaleDate(): String {
        if (!saleWidgetData.value?.data.isNullOrEmpty()) {
            saleWidgetData.value?.data?.get(0)?.ongoingCampaignEndTime?.let {
                if (it.length >= 19) {
                    val date = it.substring(0, 10)
                    val time = it.substring(11, 19)
                    return "${date}T${time}"
                }
            }
        }
        return ""

//        if (!saleWidgetData.value?.data?.get(0)?.ongoingCampaignEndTime.isNullOrEmpty()) {
//            val serverSaleDateTime = saleWidgetData.value?.data?.get(0)?.ongoingCampaignEndTime
//            val date = serverSaleDateTime?.substring(0, 10)
//            val time = serverSaleDateTime?.substring(11, 19)
//            flashSaleDate = "${date}T${time}"
//        }
//        return flashSaleDate
    }

    fun stopTimer() {
        timeCounter?.cancel()
        timeCounter = null
    }

    fun getTimerData() = mutableTimeDiffModel

    fun onLihatSemuaClicked(context: Context) {
        if (!saleWidgetData.value?.data.isNullOrEmpty()) {
            saleWidgetData.value!!.data!![0].btnApplink?.let {
                navigate(context, it)
            }
        }
    }

    override fun onStop() {
        stopTimer()
        super.onStop()
    }
}
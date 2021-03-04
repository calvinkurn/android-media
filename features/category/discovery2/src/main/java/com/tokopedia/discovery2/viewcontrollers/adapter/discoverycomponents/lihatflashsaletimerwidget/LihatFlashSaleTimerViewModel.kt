package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.lihatflashsaletimerwidget

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.Utils.Companion.parseFlashSaleDate
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.multibannerresponse.timmerwithbanner.TimerDataModel
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.banners.timerbanners.SaleCountDownTimer
import java.text.SimpleDateFormat
import java.util.*

class LihatFlashSaleTimerViewModel(val application: Application, val componentData: ComponentsItem, val position: Int) : DiscoveryBaseViewModel() {

    private val saleWidgetData: MutableLiveData<ComponentsItem> = MutableLiveData()
    private var timeCounter: SaleCountDownTimer? = null
    private val elapsedTime: Long = 1000
    private val mutableTimeDiffModel: MutableLiveData<TimerDataModel> = MutableLiveData()

    init {
        saleWidgetData.value = componentData
    }

    fun getComponentData(): LiveData<ComponentsItem> = saleWidgetData
    fun getTimerData() = mutableTimeDiffModel

    fun startTimer() {
        val parsedTimerDate = parseFlashSaleDate(componentData.data?.firstOrNull()?.ongoingCampaignEndTime)
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

    fun stopTimer() {
        timeCounter?.cancel()
        timeCounter = null
    }

    fun onLihatSemuaClicked(context: Context) {
        componentData.data?.firstOrNull()?.btnApplink?.let {
            navigate(context, it)
        }
    }

    override fun onStop() {
        stopTimer()
        super.onStop()
    }
}
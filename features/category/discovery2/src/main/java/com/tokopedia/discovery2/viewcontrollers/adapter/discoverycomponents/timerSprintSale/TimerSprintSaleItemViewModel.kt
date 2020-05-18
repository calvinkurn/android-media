package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.timerSprintSale

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.multibannerresponse.timmerwithbanner.TimerDataModel
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.banners.timerbanners.SaleCountDownTimer
import java.text.SimpleDateFormat
import java.util.*

class TimerSprintSaleItemViewModel(val application: Application, components: ComponentsItem) : DiscoveryBaseViewModel() {
    private val componentData: MutableLiveData<ComponentsItem> = MutableLiveData()
    private var timerWithBannerCounter: SaleCountDownTimer? = null
    private val elapsedTime: Long = 1000

    init {
        componentData.value = components
        TimeZone.setDefault(TimeZone.getTimeZone(Utils.TIME_ZONE))
    }

    fun getComponentLiveData(): LiveData<ComponentsItem> {
        return componentData
    }

    fun isFutureSale(): Boolean {
        val currentSystemTime = Calendar.getInstance().time
        val parsedStartDate = SimpleDateFormat(Utils.TIMER_SPRINT_SALE_DATE_FORMAT, Locale.getDefault())
                .parse(componentData.value?.data?.get(0)?.startDate)

        return currentSystemTime < parsedStartDate
    }

    fun isSaleOver(): Boolean {
        val currentSystemTime = Calendar.getInstance().time
        val parsedEndDate = SimpleDateFormat(Utils.TIMER_SPRINT_SALE_DATE_FORMAT, Locale.getDefault())
                .parse(componentData.value?.data?.get(0)?.endDate)

        return currentSystemTime > parsedEndDate
    }

    fun startTimer() {
        val currentSystemTime = Calendar.getInstance().time
        val parsedEndDate = SimpleDateFormat(Utils.TIMER_SPRINT_SALE_DATE_FORMAT, Locale.getDefault())
                .parse(componentData.value?.data?.get(0)?.endDate)

        val saleTimeMillis = parsedEndDate.time - currentSystemTime.time

        if (saleTimeMillis > 0) {
            timerWithBannerCounter = SaleCountDownTimer(saleTimeMillis, elapsedTime)
            timerWithBannerCounter?.start()
        }
    }

    // TODO Cancel countdown timer on viewHolder destroy
    fun stopTimer() {
        if (timerWithBannerCounter != null) {
            timerWithBannerCounter?.cancel()
        }
    }

    fun getTimerData(): LiveData<TimerDataModel> {
        return timerWithBannerCounter?.mutableTimeDiffModel ?: MutableLiveData<TimerDataModel>()
    }


    override fun initDaggerInject() {

    }
}

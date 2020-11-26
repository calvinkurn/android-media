package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.timerSprintSale

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.banners.timerbanners.SaleCountDownTimer
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class TimerSprintSaleItemViewModel(val application: Application, val components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel() {
    private val componentData: MutableLiveData<ComponentsItem> = MutableLiveData()
    private var timerWithBannerCounter: SaleCountDownTimer? = null
    private val elapsedTime: Long = 1000

    init {
        TimeZone.setDefault(TimeZone.getTimeZone(Utils.TIME_ZONE))
    }

    override fun onAttachToViewHolder() {
        super.onAttachToViewHolder()
        componentData.value = components
    }

    fun getComponentLiveData() = componentData

    fun isFutureSale(): Boolean {
        val startData = components.data?.get(0)?.startDate
        if (startData.isNullOrEmpty()) return false

        val currentSystemTime = Calendar.getInstance().time
        val parsedDate = parseData(startData)
        return if (parsedDate != null) {
            return currentSystemTime < parsedDate
        } else {
            false
        }
    }

    fun isSaleOver(): Boolean {
        val endDate = components.data?.get(0)?.endDate
        if (endDate.isNullOrEmpty()) return false

        val currentSystemTime = Calendar.getInstance().time
        val parsedDate = parseData(endDate)
        return if (parsedDate != null) {
            return currentSystemTime > parsedDate
        } else {
            false
        }
    }

    fun startTimer() {

        val timerData: String? = if (isFutureSale()) {
            components.data?.get(0)?.startDate
        } else {
            components.data?.get(0)?.endDate
        }
        if (!timerData.isNullOrEmpty()) {
            val currentSystemTime = Calendar.getInstance().time

            val parsedEndDate = parseData(timerData)
            if (parsedEndDate != null) {
                val saleTimeMillis = parsedEndDate.time - currentSystemTime.time
                if (saleTimeMillis > 0) {
                    timerWithBannerCounter = SaleCountDownTimer(saleTimeMillis, elapsedTime)
                    timerWithBannerCounter?.start()
                }
            }
        }
    }

    private fun parseData(date: String?): Date? {
        return try {
            SimpleDateFormat(Utils.TIMER_SPRINT_SALE_DATE_FORMAT, Locale.getDefault())
                    .parse(date)
        } catch (parseException: ParseException) {
            null
        }
    }

    // TODO Cancel countdown timer on viewHolder destroy
    fun stopTimer() {
        if (timerWithBannerCounter != null) {
            timerWithBannerCounter?.cancel()
        }
    }

    fun getTimerData() = timerWithBannerCounter?.mutableTimeDiffModel ?: MutableLiveData()

}
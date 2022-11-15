package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.contentCard

import android.app.Application
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tkpd.atcvariant.util.AtcCommonMapper.asSuccess
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.data.multibannerresponse.timmerwithbanner.TimerDataModel
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.banners.timerbanners.SaleCountDownTimer
import java.text.SimpleDateFormat
import java.util.*

class ContentCardItemViewModel(val application: Application, val components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel() {
    private val componentData: MutableLiveData<ComponentsItem> = MutableLiveData()
    private var timeCounter: SaleCountDownTimer? = null
    private val elapsedTime: Long = 1000
    private val mutableTimeDiffModel: MutableLiveData<TimerDataModel> = MutableLiveData()
    private val mutableTimerText: MutableLiveData<String> = MutableLiveData()

    override fun onAttachToViewHolder() {
        super.onAttachToViewHolder()
        componentData.value = components
    }

    private fun handleSaleEndSates() {
        if (Utils.isFutureSale(getStartDate()) ||
            (Utils.isFutureSaleOngoing(getStartDate(),
                getEndDate())) || Utils.isSaleOver(getEndDate())) {
            startTimer()
        }

    }

    fun getTimerData() = mutableTimeDiffModel

    fun getTimerText() = mutableTimerText

    fun getStartDate(): String {
        if (!components.data.isNullOrEmpty()) {
            return components.data?.firstOrNull()?.startDate ?: ""
        }
        return ""
    }

    fun getEndDate(): String {
        if (!components.data.isNullOrEmpty()) {
            return components.data?.firstOrNull()?.endDate ?: ""
        }
        return ""
    }

    fun startTimer() {
        val futureSaleTab = Utils.isFutureSale(getStartDate())
        val timerData: String? = if (futureSaleTab) getStartDate() else getEndDate()
        if (!timerData.isNullOrEmpty()) {
            val currentSystemTime = Calendar.getInstance().time
            val parsedEndDate = Utils.parseFlashSaleDate(timerData)
            SimpleDateFormat(Utils.TIMER_DATE_FORMAT, Locale.getDefault())
                .parse(parsedEndDate)?.let { parsedDate ->
                val saleTimeMillis = parsedDate.time - currentSystemTime.time
                if (saleTimeMillis > 0) {
                    if (futureSaleTab) {
                        mutableTimerText.value =
                            application.resources.getString(R.string.discovery_sale_begins_in)
                    } else {
                        mutableTimerText.value =
                            application.resources.getString(R.string.discovery_sale_ends_on)
                    }
                    timeCounter = SaleCountDownTimer(saleTimeMillis, elapsedTime) { timerModel ->
                        if (timerModel.timeFinish) {
                            stopTimer()
                            handleSaleEndSates()
                        }
                        mutableTimeDiffModel.value = timerModel
                    }
                    timeCounter?.start()
                } else {
                    mutableTimeDiffModel.value?.hours = 0
                    mutableTimeDiffModel.value?.minutes = 0
                }
            }
        }
    }

    fun stopTimer() {
        timeCounter?.cancel()
        timeCounter = null
    }

    fun getComponentLiveData(): LiveData<ComponentsItem> = componentData

    fun getNavigationUrl(): String? {
        return getDataItem()?.landingPage?.appLink
    }

    private fun getDataItem(): DataItem? {
        return components.data?.firstOrNull()
    }

    override fun onStop() {
        stopTimer()
        super.onStop()
    }
}

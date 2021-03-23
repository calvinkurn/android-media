package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.timerSprintSale

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.common.SingleLiveEvent
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.multibannerresponse.timmerwithbanner.TimerDataModel
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.banners.timerbanners.SaleCountDownTimer
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
import java.util.*


class TimerSprintSaleItemViewModel(val application: Application, val components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel() {
    private val componentData: MutableLiveData<ComponentsItem> = MutableLiveData()
    var timerWithBannerCounter: CountDownTimer? = null
    private val elapsedTime: Long = 1000
    private val needPageRefresh: MutableLiveData<Boolean> = MutableLiveData()
    private val mutableTimeDiffModel: MutableLiveData<TimerDataModel> = MutableLiveData()
    private val restartStoppedTimerEvent: SingleLiveEvent<Boolean> = SingleLiveEvent()
    private var isTimerStopped = false

    init {
        TimeZone.setDefault(TimeZone.getTimeZone(Utils.TIME_ZONE))
    }

    override fun onAttachToViewHolder() {
        super.onAttachToViewHolder()
        componentData.value = components
    }

    fun getComponentLiveData() = componentData
    fun refreshPage(): LiveData<Boolean> = needPageRefresh
    fun getTimerData(): LiveData<TimerDataModel> = mutableTimeDiffModel
    fun getRestartTimerAction(): LiveData<Boolean> = restartStoppedTimerEvent
    fun handleSaleEndSates() {
        when {
            Utils.isFutureSale(getStartDate()) || (Utils.isFutureSaleOngoing(getStartDate(), getEndDate())) -> {
                needPageRefresh.value = true
            }
            Utils.isSaleOver(getEndDate()) -> {
                this@TimerSprintSaleItemViewModel.syncData.value = true
            }
        }
    }

    fun checkUpcomingSaleTimer() {
        val pageEndPoint = components.pageEndPoint
        getComponent(components.parentComponentId, pageEndPoint)?.let { tabItemParent ->
            getComponent(tabItemParent.parentComponentId, pageEndPoint)?.let { tabs ->
                tabs.data?.let { tabItem ->
                    if (tabItem.size >= 2 && !tabItem[1].targetComponentId.isNullOrEmpty()) {
                        val targetComponentIdList = tabItem[1].targetComponentId?.split(",")?.map { it.trim() }
                        if (!targetComponentIdList.isNullOrEmpty()) {
                            targetComponentIdList.forEach { componentId ->
                                getComponent(componentId, pageEndPoint)?.let { componentItem ->
                                    checkForTimerComponent(componentItem)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun checkForTimerComponent(componentItem: ComponentsItem) {
        if (componentItem.name == ComponentNames.TimerSprintSale.componentName && !componentItem.data.isNullOrEmpty()) {
            componentItem.data?.firstOrNull()?.startDate?.let { startDate ->
                when {
                    Utils.isFutureSale(startDate) -> {
                        val currentSystemTime = Calendar.getInstance().time
                        val parsedEndDate = Utils.parseData(startDate)
                        parsedEndDate?.let {
                            val saleTimeMillis = parsedEndDate.time - currentSystemTime.time
                            if (saleTimeMillis > 0) {
                                timerWithBannerCounter = SaleCountDownTimer(saleTimeMillis, elapsedTime, false) { timerData ->
                                    if (timerData.timeFinish) {
                                        stopTimer()
                                        needPageRefresh.value = true
                                    }
                                }
                                timerWithBannerCounter?.start()
                            }
                        }
                    }
                    Utils.isFutureSaleOngoing(startDate, componentItem.data!![0].endDate
                            ?: "") -> {
                        needPageRefresh.value = true
                    }
                    else -> {
                        needPageRefresh.value = true
                    }
                }
            }
        }
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
                    checkUpcomingSaleTimer()
                }
            }
        }
    }

    override fun onDetachToViewHolder() {
        stopTimer()
    }

    fun stopTimer() {
        timerWithBannerCounter?.cancel()
        timerWithBannerCounter = null
    }

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

    fun getTimerVariant(): Int {
        var variant = TimerUnifySingle.VARIANT_MAIN
        components.properties?.timerStyle?.let { timerStyle ->
            if (timerStyle.isNotEmpty()) {
                when (timerStyle) {
                    Informative -> {
                        variant = TimerUnifySingle.VARIANT_INFORMATIVE
                    }
                    Inverted -> {
                        variant = TimerUnifySingle.VARIANT_ALTERNATE
                    }
                }
            }
        }
        return variant
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

    companion object{
        private const val Informative = "informative"
        private const val Inverted = "inverted"
    }

}
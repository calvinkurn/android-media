package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.banners.timerbanners

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.DEFAULT_TIME_DATA
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.Utils.Companion.TIMER_DATE_FORMAT
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.unifycomponents.timer.TimerUnifyHighlight
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import java.util.*

class BannerTimerViewModel(val application: Application, components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel(components) {
    private val bannerTimeData: MutableLiveData<ComponentsItem> = MutableLiveData()
    private val restartStoppedTimerEvent: SingleLiveEvent<Boolean> = SingleLiveEvent()
    private var timerWithBannerCounter: CountDownTimer? = null
    private var isTimerStopped = false

    init {
        bannerTimeData.value = components
        TimeZone.setDefault(TimeZone.getTimeZone(Utils.TIME_ZONE))
    }

    fun getBannerUrlHeight() = Utils.extractDimension(bannerTimeData.value?.data?.get(0)?.backgroundUrlMobile)
    fun getBannerUrlWidth() = Utils.extractDimension(bannerTimeData.value?.data?.get(0)?.backgroundUrlMobile, "width")
    fun getRestartTimerAction(): LiveData<Boolean> = restartStoppedTimerEvent

    fun startTimer(timer: TimerUnifyHighlight) {
        var parsingDate = ""
        val saleStartDate = components.data?.firstOrNull()?.startDate ?: ""
        val saleEndDate = components.data?.firstOrNull()?.endDate ?: ""
        val timeDiff: Long = if (Utils.isFutureSaleOngoing(saleStartDate = saleStartDate, saleEndDate = saleEndDate, TIMER_DATE_FORMAT)) {
            parsingDate = saleEndDate
            Utils.getElapsedTime(saleEndDate)
        } else {
            parsingDate = saleStartDate
            Utils.getElapsedTime(saleStartDate)
        }
        if (timeDiff > 0) {
            Utils.parseData(parsingDate, TIMER_DATE_FORMAT)?.let { date ->
                val calendar = Calendar.getInstance()
                calendar.time = date
                timer.targetDate = calendar
                timer.onTick = {
                    timer.timer?.let { countDownTimer ->
                        if (countDownTimer != timerWithBannerCounter) {
                            timerWithBannerCounter = countDownTimer
                        }
                        timer.post {
                            timer.onTick = null
                        }
                    }
                }
            }
        } else {
            this@BannerTimerViewModel.syncData.value = true
        }
    }

    fun stopTimer() {
        timerWithBannerCounter?.cancel()
        timerWithBannerCounter = null
    }

    fun getApplink() = components.data?.firstOrNull()?.applinks

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
            restartStoppedTimerEvent.postValue(true)
            isTimerStopped = false
        }
        super.onResume()
    }

    fun checkTimerEnd() {
        requireAutoRefreshEnabled {
            val saleStartDate = components.data?.firstOrNull()?.startDate.orEmpty()
            val saleEndDate = components.data?.firstOrNull()?.endDate.orEmpty()

            handleTimerEnd(saleStartDate, saleEndDate)
        }
    }

    private fun handleTimerEnd(saleStartDate: String, saleEndDate: String) {
        val isFutureSaleOngoing = Utils.isFutureSaleOngoing(
            saleStartDate = saleStartDate,
            saleEndDate = saleEndDate,
            TIMER_DATE_FORMAT
        )

        if (isFutureSaleOngoing) {
            this@BannerTimerViewModel.syncData.value = true
        } else {
            if (Utils.getElapsedTime(saleEndDate) <= DEFAULT_TIME_DATA) {
                this@BannerTimerViewModel.syncData.value = true
            }
        }
    }

    private fun requireAutoRefreshEnabled(action: () -> Unit) {
        val shouldAutoRefresh = components.properties?.shouldAutoRefresh ?: false
        if (!shouldAutoRefresh) return

        action.invoke()
    }

    fun getTimerVariant(): Int {
        var variant = TimerUnifyHighlight.VARIANT_LIGHT_RED
        components.data?.firstOrNull()?.let {
            if (!it.variant.isNullOrEmpty()) {
                when (it.variant) {
                    ALTERNATE -> {
                        variant = TimerUnifyHighlight.VARIANT_ALTERNATE
                    }
                    MAIN -> {
                        if (!it.color.isNullOrEmpty()) {
                            when (it.color) {
                                RED_DARK ->
                                    variant = TimerUnifyHighlight.VARIANT_DARK_RED
                                RED_LIGHT ->
                                    variant = TimerUnifyHighlight.VARIANT_LIGHT_RED
                            }
                        }
                    }
                }
            }
        }
        return variant
    }

    companion object {
        const val ALTERNATE = "alternate"
        const val MAIN = "main"
        const val RED_LIGHT = "redLight"
        const val RED_DARK = "redDark"
    }
}

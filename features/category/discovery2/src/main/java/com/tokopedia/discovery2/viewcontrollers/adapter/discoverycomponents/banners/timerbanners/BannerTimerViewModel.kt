package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.banners.timerbanners

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.multibannerresponse.timmerwithbanner.TimerDataModel
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel

class BannerTimerViewModel(val application: Application, val components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel() {
    private val bannerTimeData: MutableLiveData<ComponentsItem> = MutableLiveData()
    private var timerWithBannerCounter: SaleCountDownTimer? = null
    private val elapsedTime: Long = 1000
    private val SHOW_DAYS: Boolean = true
    private var isTimerStopped = false

    init {
        bannerTimeData.value = components
    }

    fun getBannerUrlHeight() = Utils.extractDimension(bannerTimeData.value?.data?.get(0)?.backgroundUrlMobile)
    fun getBannerUrlWidth() = Utils.extractDimension(bannerTimeData.value?.data?.get(0)?.backgroundUrlMobile, "width")
    private val mutableTimeDiffModel: MutableLiveData<TimerDataModel> = MutableLiveData()

    fun startTimer() {
        val timeDiff = Utils.getElapsedTime(components.data?.firstOrNull()?.endDate ?: "")
        if (timeDiff > 0) {
            timerWithBannerCounter = SaleCountDownTimer(timeDiff, elapsedTime, SHOW_DAYS) {
                mutableTimeDiffModel.value = it
            }
            timerWithBannerCounter?.start()
        }
    }

    fun stopTimer() {
        timerWithBannerCounter?.cancel()
        timerWithBannerCounter = null
    }

    fun getTimerData() = mutableTimeDiffModel

    fun onBannerClicked(context: Context) {
        bannerTimeData.value?.data?.firstOrNull()?.let {
            navigate(context, it.applinks)
        }
    }

    fun getComponent() = components

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
            startTimer()
            isTimerStopped = false
        }
        super.onResume()
    }

    fun checkTimerEnd(timerDataModel: TimerDataModel) {
        if (timerDataModel.days == 0 && timerDataModel.hours == 0 &&
                timerDataModel.minutes == 0 && timerDataModel.seconds == 0) {
            this@BannerTimerViewModel.syncData.value = true
        }
    }
}
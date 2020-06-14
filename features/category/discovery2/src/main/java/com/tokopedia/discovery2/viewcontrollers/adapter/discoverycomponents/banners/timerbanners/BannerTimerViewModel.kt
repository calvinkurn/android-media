package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.banners.timerbanners

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.multibannerresponse.timmerwithbanner.TimerDataModel
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import java.text.SimpleDateFormat
import java.util.*

class BannerTimerViewModel(val application: Application, components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel() {
    private val bannerTimeData: MutableLiveData<ComponentsItem> = MutableLiveData()
    private var timerWithBannerCounter: SaleCountDownTimer? = null
    private val elapsedTime: Long = 1000

    init {
        bannerTimeData.value = components
    }

    fun getComponentData(): LiveData<ComponentsItem> = bannerTimeData
    fun getBannerUrlHeight() = Utils.extractDimension(bannerTimeData.value?.data?.get(0)?.backgroundUrlMobile)
    fun getBannerUrlWidth() = Utils.extractDimension(bannerTimeData.value?.data?.get(0)?.backgroundUrlMobile, "width")


    fun startTimer() {
        TimeZone.setDefault(TimeZone.getTimeZone(Utils.TIME_ZONE))
        val currentSystemTime = Calendar.getInstance().time
        val parsedEndDate = SimpleDateFormat(Utils.TIMER_DATE_FORMAT, Locale.getDefault())
                .parse(bannerTimeData.value?.data?.get(0)?.endDate)
        val saleTimeMillis = parsedEndDate.time - currentSystemTime.time

        if (saleTimeMillis > 0) {
            timerWithBannerCounter = SaleCountDownTimer(saleTimeMillis, elapsedTime)
            timerWithBannerCounter?.start()
        }
    }

    fun stopTimer() {
        if (timerWithBannerCounter != null) {
            timerWithBannerCounter!!.cancel()
        }
    }

    fun getTimerData(): LiveData<TimerDataModel> {
        return timerWithBannerCounter?.mutableTimeDiffModel ?: MutableLiveData()
    }

    fun onBannerClicked(context: Context) {
        bannerTimeData.value?.data?.firstOrNull()?.let {
            navigate(context, it.applinks)
        }
    }
}
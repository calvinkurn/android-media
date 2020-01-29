package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.carouselbanner

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.utils.Utils
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel

class CarouselBannerItemViewModel (val application: Application, components: ComponentsItem) : DiscoveryBaseViewModel() {

    val componentData: MutableLiveData<ComponentsItem> = MutableLiveData()

    init {
        componentData.value = components
    }

    fun getBannerUrlHeight() = Utils.extractDimension(componentData.value?.data?.get(0)?.imageUrlDynamicMobile)
    fun getBannerUrlWidth() = Utils.extractDimension(componentData.value?.data?.get(0)?.imageUrlDynamicMobile, "width")

    override fun initDaggerInject() {

    }
}
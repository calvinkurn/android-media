package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.bannercarousel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.discoverymapper.DiscoveryDataMapper
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel

class BannerCarouselViewModel(val application: Application, val component: ComponentsItem, val position: Int) : DiscoveryBaseViewModel() {
    private val bannerCarouselList: MutableLiveData<ArrayList<ComponentsItem>> = MutableLiveData()
    private val title: MutableLiveData<String> = MutableLiveData()
    private val componentData: MutableLiveData<ComponentsItem> = MutableLiveData()

    init {
        componentData.value = component
        component.data?.let {
            if (it.isNotEmpty()) {
                bannerCarouselList.value = DiscoveryDataMapper.mapListToComponentList(it, ComponentNames.BannerCarouselItemView.componentName,
                        component.name, position, component.properties?.design
                        ?: "")
                title.value = component.properties?.bannerTitle ?: ""
            }
        }
    }

    fun getComponentData(): LiveData<ArrayList<ComponentsItem>> = bannerCarouselList
    fun getTitleLiveData(): LiveData<String> = title
    fun getComponents(): LiveData<ComponentsItem> = componentData

    fun getLihatUrl(): String {
        component.properties?.let {
            return it.ctaApp ?: ""
        }
        return ""
    }
}
package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.sliderbanner

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.discoverymapper.DiscoveryDataMapper
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

class SliderBannerViewModel(val application: Application, components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel(), CoroutineScope {

    private val componentsData: MutableLiveData<ComponentsItem> = MutableLiveData()
    private val listData: MutableLiveData<ArrayList<ComponentsItem>> = MutableLiveData()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

    init {
        componentsData.value = components
        components.data?.let {
            listData.value = DiscoveryDataMapper.mapListToComponentList(it, ComponentNames.SingleBanner.componentName, components.name, position)
        }
    }

    fun getComponentsLiveData(): LiveData<ComponentsItem> {
        return componentsData
    }

    fun getListDataLiveData(): LiveData<ArrayList<ComponentsItem>> {
        return listData
    }




}
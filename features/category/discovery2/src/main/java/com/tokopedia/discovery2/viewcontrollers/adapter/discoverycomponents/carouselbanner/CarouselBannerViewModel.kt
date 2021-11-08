package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.carouselbanner

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

class CarouselBannerViewModel(application: Application, components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel(), CoroutineScope {

    private val componentData: MutableLiveData<ComponentsItem> = MutableLiveData()
    private val listData: MutableLiveData<ArrayList<ComponentsItem>> = MutableLiveData()
    private val seeAllButton: MutableLiveData<String> = MutableLiveData()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

    init {
        componentData.value = components
        components.data?.let {
            if (it.isNotEmpty()) {
                listData.value = DiscoveryDataMapper.mapListToComponentList(it, ComponentNames.CarouselBannerItemView.componentName, components.name, position)
                seeAllButton.value = it[0].buttonApplink
            }
        }
    }

    fun getComponentLiveData(): LiveData<ComponentsItem> {
        return componentData
    }

    fun getListDataLiveData(): LiveData<ArrayList<ComponentsItem>> {
        return listData
    }

    fun getSeeAllButtonLiveData(): LiveData<String> {
        return seeAllButton
    }

    fun getComponentPosition() = position
}
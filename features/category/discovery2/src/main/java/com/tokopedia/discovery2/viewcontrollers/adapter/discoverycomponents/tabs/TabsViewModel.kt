package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.tabs

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.discoverymapper.DiscoveryDataMapper
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

class TabsViewModel(val application: Application, components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel(), CoroutineScope {
    private val listData: MutableLiveData<ArrayList<ComponentsItem>> = MutableLiveData()

    init {
        components.data?.let {
            listData.value = DiscoveryDataMapper.mapTabsListToComponentList(it, ComponentNames.TabsItem.componentName, position)
        }
    }

    fun getListDataLiveData(): MutableLiveData<ArrayList<ComponentsItem>> {
        return listData
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

    override fun initDaggerInject() {

    }

}
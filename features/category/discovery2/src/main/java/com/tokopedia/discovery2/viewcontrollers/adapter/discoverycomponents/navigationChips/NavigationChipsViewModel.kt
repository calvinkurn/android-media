package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.navigationChips

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.discoverymapper.DiscoveryDataMapper
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel

class NavigationChipsViewModel (application: Application, val components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel() {
    private val listData: MutableLiveData<ArrayList<ComponentsItem>> = MutableLiveData()

    init {
        listData.value = DiscoveryDataMapper().mapListToComponentList(components.data, ComponentNames.NavigationChipsItem.componentName, properties = null)
    }

    fun getListDataLiveData(): MutableLiveData<ArrayList<ComponentsItem>> {
        return listData
    }

}
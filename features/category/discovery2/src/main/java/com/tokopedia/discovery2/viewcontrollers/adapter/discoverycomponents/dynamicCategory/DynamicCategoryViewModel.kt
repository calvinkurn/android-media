package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.dynamicCategory

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.discoverymapper.DiscoveryDataMapper
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel

class DynamicCategoryViewModel(application: Application, components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel() {
    private val componentData: MutableLiveData<ArrayList<ComponentsItem>> = MutableLiveData()

    init {
        components.data?.let {
            componentData.value = DiscoveryDataMapper.mapListToComponentList(it, ComponentNames.DynamicCategoryItem.componentName, components.name, position)
        }
    }

    fun getComponentLiveData(): LiveData<ArrayList<ComponentsItem>> {
        return componentData
    }

}
package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.categorynavigation

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel

class CategoryNavigationItemViewModel(val application: Application, private val components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel() {
    private val componentData: MutableLiveData<ComponentsItem> = MutableLiveData()

    init {
        componentData.value = components
    }

    override fun initDaggerInject() {

    }

    fun getComponentData(): LiveData<ComponentsItem> {
        return componentData
    }
}
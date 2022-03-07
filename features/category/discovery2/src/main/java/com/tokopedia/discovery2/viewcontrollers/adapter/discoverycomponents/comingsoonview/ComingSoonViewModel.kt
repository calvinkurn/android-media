package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.comingsoonview

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel

class ComingSoonViewModel(application: Application, components: ComponentsItem, position: Int) : DiscoveryBaseViewModel() {

    private val componentData: MutableLiveData<ComponentsItem> = MutableLiveData()

    init {
        componentData.value = components
    }

    fun getComponent(): LiveData<ComponentsItem> {
        return componentData
    }
}
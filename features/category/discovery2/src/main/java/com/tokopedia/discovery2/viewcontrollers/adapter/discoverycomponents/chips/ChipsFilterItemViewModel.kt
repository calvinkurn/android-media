package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.chips

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel

class ChipsFilterItemViewModel(application: Application, components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel() {

    private val componentData: MutableLiveData<ComponentsItem> = MutableLiveData()

    init {
        componentData.value = components
    }

    fun getComponentLiveData(): LiveData<ComponentsItem> {
        return componentData
    }
}
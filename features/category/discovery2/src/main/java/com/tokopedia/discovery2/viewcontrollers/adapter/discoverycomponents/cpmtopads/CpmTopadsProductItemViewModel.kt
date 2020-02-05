package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.cpmtopads

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel

class CpmTopadsProductItemViewModel(val application: Application, components: ComponentsItem) : DiscoveryBaseViewModel() {

    val componentData: MutableLiveData<ComponentsItem> = MutableLiveData()

    init {
        componentData.value = components
    }

    fun getComponent(): LiveData<ComponentsItem> {
        return componentData
    }

    override fun initDaggerInject() {

    }

}
package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.shimmer

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel

class ShimmerViewModel(val application: Application, private val components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel() {
    override fun initDaggerInject() {

    }

    private val componentData: MutableLiveData<ComponentsItem> = MutableLiveData()


    fun getComponentData(): LiveData<ComponentsItem> {
        componentData.value = components
        return componentData
    }

}

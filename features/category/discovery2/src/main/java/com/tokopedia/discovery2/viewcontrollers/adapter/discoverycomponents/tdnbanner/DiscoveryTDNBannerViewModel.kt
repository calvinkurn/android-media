package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.tdnbanner

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel

class DiscoveryTDNBannerViewModel(
    application: Application,
    components: ComponentsItem,
    val position: Int
) : DiscoveryBaseViewModel(components) {

    private val componentMutableLiveData : MutableLiveData<ComponentsItem> = MutableLiveData<ComponentsItem>()
    val componentLiveData:LiveData<ComponentsItem> = componentMutableLiveData

    init {
        componentMutableLiveData.value = components
    }
}

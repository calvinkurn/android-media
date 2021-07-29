package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.tdnbanner

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel

class DiscoveryTDNBannerViewModel(application: Application,val components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel() {

    private val _componentsItemLD: MutableLiveData<ComponentsItem> = MutableLiveData<ComponentsItem>()

    val componentLiveData:LiveData<ComponentsItem> = _componentsItemLD

    init {
        _componentsItemLD.value = components
    }

}
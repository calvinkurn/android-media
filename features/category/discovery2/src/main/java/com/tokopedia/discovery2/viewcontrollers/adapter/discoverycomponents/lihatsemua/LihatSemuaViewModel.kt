package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.lihatsemua

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel

class LihatSemuaViewModel(val application: Application, componentData: ComponentsItem, val position: Int) : DiscoveryBaseViewModel() {
    private val itemData: MutableLiveData<ComponentsItem> = MutableLiveData()

    init {
        itemData.value = componentData
    }

    fun getComponentData(): LiveData<ComponentsItem> = itemData

}
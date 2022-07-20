package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.textcomponent

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel

class TextComponentViewModel(application: Application, private val components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel() {

    private var textComponentLiveData: MutableLiveData<DataItem> = MutableLiveData()

    init {
        textComponentLiveData.value = components.data?.firstOrNull()
    }

    fun getTextComponentLiveData(): LiveData<DataItem> = textComponentLiveData

}
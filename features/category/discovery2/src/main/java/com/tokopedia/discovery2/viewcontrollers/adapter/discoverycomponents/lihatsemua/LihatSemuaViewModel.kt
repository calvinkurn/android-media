package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.lihatsemua

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.tokopedia.applink.RouteManager
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel

class LihatSemuaViewModel(val application: Application, private val componentData: ComponentsItem, val position:Int) : DiscoveryBaseViewModel() {
    private val itemData: MutableLiveData<ComponentsItem> = MutableLiveData()

    override fun initDaggerInject() {
    }

    init {
        itemData.value = componentData
    }


    fun getComponentData() = itemData

    fun onButtonClicked() {
        if (!componentData.data?.get(0)?.btnApplink?.isEmpty()!!) {
            RouteManager.route(application, componentData.data!!.get(0).btnApplink)
        }
    }
}
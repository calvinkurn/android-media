package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.brandrecommendations

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.discoverymapper.DiscoveryDataMapper
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

class BrandRecommendationViewModel(val application: Application, components: ComponentsItem) : DiscoveryBaseViewModel(), CoroutineScope {

    val componentData: MutableLiveData<ComponentsItem> = MutableLiveData()
    val listData: MutableLiveData<ArrayList<ComponentsItem>> = MutableLiveData()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()


    init {
        componentData.value = components
        components.data?.let {
            listData.value = DiscoveryDataMapper.mapListToComponentList(it, ComponentNames.BrandRecommendationItem.componentName)
        }
    }

    override fun initDaggerInject() {
    }


}
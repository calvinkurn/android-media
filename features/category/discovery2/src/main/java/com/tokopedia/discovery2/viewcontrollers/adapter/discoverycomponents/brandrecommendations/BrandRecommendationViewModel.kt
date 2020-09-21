package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.brandrecommendations

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.discoverymapper.DiscoveryDataMapper
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext
import com.tokopedia.discovery2.Constant.BrandRecommendation.SQUARE_DESIGN


class BrandRecommendationViewModel(val application: Application, components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel(), CoroutineScope {

    private val componentData: MutableLiveData<ComponentsItem> = MutableLiveData()
    private val listData: MutableLiveData<ArrayList<ComponentsItem>> = MutableLiveData()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()


    init {
        componentData.value = components
        components.data?.let {
            listData.value = DiscoveryDataMapper.mapListToComponentList(it, ComponentNames.BrandRecommendationItem.componentName,
                    components.name, position, components.properties?.type ?: SQUARE_DESIGN)
        }
    }

    fun getComponentDataLiveData(): LiveData<ComponentsItem> = componentData
    fun getListDataLiveData(): LiveData<ArrayList<ComponentsItem>> = listData
    fun getComponentPosition() = position

}
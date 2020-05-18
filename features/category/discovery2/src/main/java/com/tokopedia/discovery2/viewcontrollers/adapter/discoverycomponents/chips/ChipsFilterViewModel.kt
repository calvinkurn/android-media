package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.chips

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

class ChipsFilterViewModel(val application: Application, components: ComponentsItem) : DiscoveryBaseViewModel(), CoroutineScope {

    private val listData: MutableLiveData<ArrayList<ComponentsItem>> = MutableLiveData()

    init {
        components.data?.let {
            listData.value = DiscoveryDataMapper.mapListToComponentList(it, ComponentNames.ChipsFilterItem.componentName)
        }
    }

    fun getListDataLiveData(): MutableLiveData<ArrayList<ComponentsItem>> {
        return listData
    }

    //temp code
    fun setAdapterPositionToChildItems(position: Int){
        listData.value?.let {list ->
            list.forEach { it.data?.forEach { it.positionForParentItem = position } }
        }
    }

    override fun initDaggerInject() {

    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

}
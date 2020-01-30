package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.sliderbanner

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

class SliderBannerViewModel(val application: Application, components: ComponentsItem) : DiscoveryBaseViewModel(), CoroutineScope {

    private val componentsData: MutableLiveData<ComponentsItem> = MutableLiveData()
    private val listData: MutableLiveData<ArrayList<ComponentsItem>> = MutableLiveData()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

    init {
        componentsData.value = components
        listData.value = convertToComponentList(components)
    }

    private fun convertToComponentList(components: ComponentsItem): ArrayList<ComponentsItem> {
        val list = ArrayList<ComponentsItem>()
        components.data?.forEach {
            val componentsItem = ComponentsItem()
            componentsItem.name = "slider_banner_item"
            val dataItem = mutableListOf<DataItem>()
            dataItem.add(it)
            componentsItem.data = dataItem
            list.add(componentsItem)
        }
        return list
    }

    fun getComponentsLiveData(): LiveData<ComponentsItem> {
        return componentsData
    }

    fun getListDataLiveData(): LiveData<ArrayList<ComponentsItem>> {
        return listData
    }

    override fun initDaggerInject() {

    }


}
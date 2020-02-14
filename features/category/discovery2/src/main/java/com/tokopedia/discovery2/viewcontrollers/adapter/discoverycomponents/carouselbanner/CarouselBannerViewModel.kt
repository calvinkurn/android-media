package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.carouselbanner

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

class CarouselBannerViewModel(val application: Application, components: ComponentsItem) : DiscoveryBaseViewModel(), CoroutineScope {

    private val componentData: MutableLiveData<ComponentsItem> = MutableLiveData()
    private val listData: MutableLiveData<ArrayList<ComponentsItem>> = MutableLiveData()
    private val seeAllButton: MutableLiveData<String> = MutableLiveData()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

    init {
        componentData.value = components
        listData.value = convertToComponentList(components)
        seeAllButton.value = components.data?.get(0)?.buttonApplink
    }

    private fun convertToComponentList(components: ComponentsItem): ArrayList<ComponentsItem> {
        val list = ArrayList<ComponentsItem>()
        components.data?.forEach {
            val componentsItem = ComponentsItem()
            componentsItem.name = "carousel_banner_item"
            val dataItem = mutableListOf<DataItem>()
            dataItem.add(it)
            componentsItem.data = dataItem
            list.add(componentsItem)
        }
        return list
    }

    fun getComponentLiveData(): LiveData<ComponentsItem> {
        return componentData
    }

    fun getListDataLiveData(): LiveData<ArrayList<ComponentsItem>> {
        return listData
    }

    fun getSeeAllButtonLiveData(): LiveData<String> {
        return seeAllButton
    }

    override fun initDaggerInject() {

    }

}
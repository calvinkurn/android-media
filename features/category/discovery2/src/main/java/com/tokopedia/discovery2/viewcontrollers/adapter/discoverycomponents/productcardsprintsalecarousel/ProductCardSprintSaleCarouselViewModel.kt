package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardsprintsalecarousel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

class ProductCardSprintSaleCarouselViewModel(val application: Application, components: ComponentsItem) : DiscoveryBaseViewModel(), CoroutineScope {

    val componentData: MutableLiveData<ComponentsItem> = MutableLiveData()
    val listData: MutableLiveData<ArrayList<ComponentsItem>> = MutableLiveData()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()


    init {
        componentData.value = components
        listData.value = convertToComponentList(components)
    }

    private fun convertToComponentList(components: ComponentsItem): ArrayList<ComponentsItem> {
        val list = ArrayList<ComponentsItem>()
        components.data?.forEach {
            val componentsItem = ComponentsItem()
            componentsItem.name = "product_card_item"
            val dataItem = mutableListOf<DataItem>()
            dataItem.add(it)
            componentsItem.data = dataItem
            list.add(componentsItem)
        }
        return list
    }


    override fun initDaggerInject() {
    }


}
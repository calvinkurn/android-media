package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.tabs

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

class TabsViewModel(val application: Application, val components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel(), CoroutineScope {
    private val listData: MutableLiveData<ArrayList<ComponentsItem>> = MutableLiveData()
    private val selectedTabComponentsItem:MutableLiveData<ArrayList<ComponentsItem>> = MutableLiveData()

    init {
        components.componentsItem?.let {
            listData.value = it as ArrayList<ComponentsItem>
            }
        updateSelectedTabValue(null)
        }


    fun getListDataLiveData(): LiveData<ArrayList<ComponentsItem>> {
        return listData
    }
    fun getSelectedTabDataLiveData(): LiveData<ArrayList<ComponentsItem>> {
        return selectedTabComponentsItem
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

    override fun initDaggerInject() {

    }

    fun onTabClick(id: String) {
        updateSelectedTabValue(id)
    }


   private fun updateSelectedTabValue(id:String?) {
       val selectedID  = id?: components.componentsItem?.get(0)?.id.toString()
       components.componentsItem?.forEach { components ->
           components.data?.get(0)?.let {dataItem ->
               dataItem.isSelected = components.id == selectedID
               if(dataItem.isSelected) {
                   selectedTabComponentsItem.value = components.componentsItem as ArrayList<ComponentsItem>?
               }

           }
       }
   }

}
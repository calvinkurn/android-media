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
    private val selectedTabComponentsItem: MutableLiveData<ArrayList<ComponentsItem>> = MutableLiveData()

    override fun onAttachToViewHolder() {
        super.onAttachToViewHolder()
        components.getComponentsItem()?.let {
            listData.value = it as ArrayList<ComponentsItem>
        }
        updateSelectedTabValue(null)
    }


    fun getListDataLiveData(): LiveData<ArrayList<ComponentsItem>> {
        return listData
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

    override fun initDaggerInject() {

    }

    fun onTabClick(id: String) {
        updateSelectedTabValue(id)
        this.syncData.value = true
    }


    private fun updateSelectedTabValue(id: String?) {
        val selectedID = id ?: getSelectedId(components)
        components.getComponentsItem()?.forEach { components ->
            components.data?.get(0)?.let { dataItem ->
                dataItem.isSelected = components.id == selectedID
                if (dataItem.isSelected) {
                    selectedTabComponentsItem.value = components.getComponentsItem() as ArrayList<ComponentsItem>?
                }

            }
        }
    }

    private fun getSelectedId(components: ComponentsItem): String {
        var selectedID: String? = null
        for (it in components.getComponentsItem()!!) {
            if (it.data?.get(0)?.isSelected!!) {
                selectedID = it.id
            }
        }
        return selectedID ?: components.getComponentsItem()?.get(0)?.id.toString()
    }

}
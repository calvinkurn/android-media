package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.tabs

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel

class TabsItemIconViewModel(
    val application: Application,
    var components: ComponentsItem,
    val position: Int
) : DiscoveryBaseViewModel() {


    private val componentData: MutableLiveData<ComponentsItem> = MutableLiveData()
    private val onSelectedChangeLiveData: MutableLiveData<Boolean> = MutableLiveData()

    fun getComponentLiveData(): LiveData<ComponentsItem> {
        return componentData
    }

    fun getSelectionChangeLiveData(): LiveData<Boolean> {
        return onSelectedChangeLiveData
    }

    fun setSelectionTabItem(isSelected: Boolean) {
        if (components.data?.isNotEmpty() == true) {
            components.data?.get(0)?.isSelected = isSelected
            onSelectedChangeLiveData.value = isSelected
        }
    }

    override fun onAttachToViewHolder() {
        super.onAttachToViewHolder()
        componentData.value = components
    }

}

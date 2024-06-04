package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.tabs

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel

class PlainTabItemViewModel(
    val application: Application,
    components: ComponentsItem,
    val position: Int
) : DiscoveryBaseViewModel(components) {

    private val onSelectedData: MutableLiveData<DataItem> = MutableLiveData()
    private val onUnselectedData: MutableLiveData<Pair<DataItem, String?>> = MutableLiveData()

    fun getSelectedLiveData(): LiveData<DataItem> {
        return onSelectedData
    }

    fun getUnselectedLiveData(): LiveData<Pair<DataItem, String?>> {
        return onUnselectedData
    }

    fun setSelectionTabItem(isSelected: Boolean) {
        if (components.data?.isEmpty() == true) return

        components.data?.firstOrNull()?.isSelected = isSelected

        if (isSelected) {
            components.data?.firstOrNull()?.let {
                onSelectedData.value = it
            }
        }
    }

    fun setUnselectedTabColor(hexColor: String?) {
        components.data?.firstOrNull()?.let {
            onUnselectedData.value = it to hexColor
        }
    }
}

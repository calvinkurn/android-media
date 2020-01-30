package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.carouselbanner

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.customview.CustomViewState

class CarouselBannerItemViewModel (val application: Application, components: ComponentsItem) : DiscoveryBaseViewModel() {

    private val componentData: MutableLiveData<ComponentsItem> = MutableLiveData()
    private val description: MutableLiveData<CustomViewState> = MutableLiveData()

    init {
        componentData.value = components
        getDescriptionData(components)
    }

    private fun getDescriptionData(item: ComponentsItem) {
        val itemData = item.data?.get(0)
        itemData?.description.let {
            if (!it.isNullOrBlank()) {
                description.value = CustomViewState.ShowText(it)
            } else {
                description.value = CustomViewState.HideView
            }
        }
    }

    fun getComponentLiveData():LiveData<ComponentsItem> {
        return componentData
    }

    fun getDescriptionLiveData():LiveData<CustomViewState>{
        return description
    }
}
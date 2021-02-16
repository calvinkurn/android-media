package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.brandrecommendations

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel

class BrandRecommendationItemViewModel(val application: Application, val components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel() {
    private val componentData: MutableLiveData<ComponentsItem> = MutableLiveData()

    override fun onAttachToViewHolder() {
        super.onAttachToViewHolder()
        componentData.value = components
    }
    fun getComponentDataLiveData(): LiveData<ComponentsItem> = componentData

    fun  getDesignType(): String {
        return components.design
    }

    fun getComponentItem(): DataItem? {
        return components.data?.firstOrNull()
    }

}

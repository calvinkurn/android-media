package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.brandrecommendations

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.Constant.BrandRecommendation.SQUARE_DESIGN

class BrandRecommendationItemViewModel(val application: Application, components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel() {

    private val componentData: MutableLiveData<ComponentsItem> = MutableLiveData()

    init {
        componentData.value = components
    }

    fun getComponentDataLiveData(): LiveData<ComponentsItem> = componentData

    fun  getDesignType(): String {
        return componentData.value?.design ?: SQUARE_DESIGN
    }

}
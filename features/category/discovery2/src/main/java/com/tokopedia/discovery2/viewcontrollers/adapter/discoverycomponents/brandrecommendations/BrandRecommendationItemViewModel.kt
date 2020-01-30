package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.brandrecommendations

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel

class BrandRecommendationItemViewModel(val application: Application, components: ComponentsItem) : DiscoveryBaseViewModel() {

    val ComponentData: MutableLiveData<ComponentsItem> = MutableLiveData()

    init {
        ComponentData.value = components
    }

    override fun initDaggerInject() {

    }

}
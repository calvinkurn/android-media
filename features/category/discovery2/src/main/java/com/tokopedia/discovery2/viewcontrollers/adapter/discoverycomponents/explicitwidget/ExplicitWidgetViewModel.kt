package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.explicitwidget

import android.app.Application
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.utils.lifecycle.SingleLiveEvent

class ExplicitWidgetViewModel(val application: Application, val components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel() {
    private val explicitWidgetData: SingleLiveEvent<ComponentsItem> = SingleLiveEvent()
    var isExplicitWidgetHidden: Boolean = false

    fun getComponentData(): SingleLiveEvent<ComponentsItem> = explicitWidgetData

    override fun onAttachToViewHolder() {
        super.onAttachToViewHolder()
        explicitWidgetData.value = components
    }

    fun setWidgetHiddenState(state: Boolean) {
        components.isExplicitWidgetHidden = state
    }

}
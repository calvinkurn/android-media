package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.thematicheader

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel

class ThematicHeaderViewModel(
    application: Application,
    val component: ComponentsItem,
    val position: Int
) : DiscoveryBaseViewModel() {
    private val componentData: MutableLiveData<ComponentsItem> = MutableLiveData()

    fun getComponentLiveData(): LiveData<ComponentsItem> = componentData

    override fun onAttachToViewHolder() {
        super.onAttachToViewHolder()
        componentData.value = component
    }

    fun fetchLottieState(): Boolean {
        return component.isLottieAlreadyAnimated
    }

    fun setLottieState(state: Boolean) {
        component.isLottieAlreadyAnimated = state
    }
}

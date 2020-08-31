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

const val TAB_DEFAULT_BACKGROUND = "plain"

class TabsViewModel(val application: Application, val components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel(), CoroutineScope {
    private val setColorTabs: MutableLiveData<ArrayList<ComponentsItem>> = MutableLiveData()
    private val setUnifyTabs: MutableLiveData<ArrayList<ComponentsItem>> = MutableLiveData()

    override fun onAttachToViewHolder() {
        super.onAttachToViewHolder()

        components.getComponentsItem()?.let {
            it as ArrayList<ComponentsItem>
            if (components.properties?.background == TAB_DEFAULT_BACKGROUND) {
                setUnifyTabs.value = it
            } else {
                setColorTabs.value = it
            }

        }
    }


    fun getColorTabComponentLiveData(): LiveData<ArrayList<ComponentsItem>> {
        return setColorTabs
    }

    fun getUnifyTabLiveData(): LiveData<ArrayList<ComponentsItem>> {
        return setUnifyTabs
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

    override fun initDaggerInject() {

    }

    fun setSelectedState(position: Int, selection: Boolean): Boolean {
        if (components.getComponentsItem()?.isNotEmpty() == true) {
            val itemData = components.getComponentsItem()?.get(position)
            if (itemData?.data?.isNotEmpty() == true) {
                if (itemData.data?.get(0)?.isSelected == selection) return false
                itemData.data?.get(0)?.isSelected = selection
            }
        }
        return true
    }

    fun onTabClick() {
        this.syncData.value = true
    }

}
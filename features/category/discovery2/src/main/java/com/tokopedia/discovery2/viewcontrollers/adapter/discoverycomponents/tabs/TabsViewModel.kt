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
    private val setColorTabs: MutableLiveData<ArrayList<ComponentsItem>> = MutableLiveData()
    private val setUnifyTabs: MutableLiveData<ArrayList<ComponentsItem>> = MutableLiveData()

    override fun onAttachToViewHolder() {
        super.onAttachToViewHolder()

        components.getComponentsItem()?.let {
            it as ArrayList<ComponentsItem>
            if(components.properties?.background == "plain") {
                setUnifyTabs.value = it
            }else {
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

    fun setSelectedState(position: Int,selection:Boolean) {
        components.getComponentsItem()?.get(position)?.data?.get(0)?.isSelected = selection
    }

    fun onTabClick() {
        this.syncData.value = true
    }

}
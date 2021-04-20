package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.chips

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.di.DaggerDiscoveryComponent
import com.tokopedia.discovery2.discoverymapper.DiscoveryDataMapper
import com.tokopedia.discovery2.usecase.ChipSelectionUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class ChipsFilterViewModel(val application: Application, val components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel(), CoroutineScope {

    private val listData: MutableLiveData<ArrayList<ComponentsItem>> = MutableLiveData()

    @Inject
    lateinit var chipSelectionUseCase: ChipSelectionUseCase


    init {
        components.data?.let {
            components.setComponentsItem(DiscoveryDataMapper.mapListToComponentList(it, ComponentNames.ChipsFilterItem.componentName, components.name, position))
        }
    }

    override fun onAttachToViewHolder() {
        super.onAttachToViewHolder()
        listData.value = components.getComponentsItem() as ArrayList<ComponentsItem>?
    }

    fun getListDataLiveData(): MutableLiveData<ArrayList<ComponentsItem>> {
        return listData
    }

    fun onChipSelected(id: String?) {
        components.getComponentsItem()?.forEach {
            it.data?.get(0)?.chipSelectionType = if (it.id == id) SELECTED else NORMAL
        }
        listData.value = components.getComponentsItem() as ArrayList<ComponentsItem>
        id?.let {
            launchCatchError(block = {
                if (chipSelectionUseCase.onChipSelection(components.id, components.pageEndPoint, it)) {
                    syncData.value = true
                }
            }, onError = {
                it.printStackTrace()
            })
        }
    }

    fun onChipUnSelected(id: String?) {
        components.getComponentsItem()?.forEach {
            if (it.id == id)
                it.data?.get(0)?.chipSelectionType = NORMAL
        }
        listData.value = components.getComponentsItem() as ArrayList<ComponentsItem>
        id?.let {
            launchCatchError(block = {
                if (chipSelectionUseCase.onChipUnSelection(components.id, components.pageEndPoint)) {
                    syncData.value = true
                }
            }, onError = {
                it.printStackTrace()
            })
        }
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

}
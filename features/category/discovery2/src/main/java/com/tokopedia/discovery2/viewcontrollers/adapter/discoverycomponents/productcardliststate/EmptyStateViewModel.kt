package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardliststate

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.emptystate.EmptyStateModel
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.repository.emptystate.EmptyStateRepository
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import javax.inject.Inject

class EmptyStateViewModel(val application: Application, val components: ComponentsItem, val position: Int) : DiscoveryBaseViewModel(){
    @Inject
    lateinit var emptyStateRepository: EmptyStateRepository
    private val needReSyncMutableLiveData = MutableLiveData<Boolean>()
    val needReSyncLiveData: LiveData<Boolean>
        get() = needReSyncMutableLiveData

    fun getEmptyStateData() : EmptyStateModel {
        return emptyStateRepository.getEmptyStateData(components)
    }

    fun resetChildComponents() {
        getComponent(components.parentComponentId, components.pageEndPoint)?.let {
            it.setComponentsItem(null, components.tabName)
            it.selectedFilters = null
            it.selectedSort = null
            it.noOfPagesLoaded = 0
            getComponent(it.parentComponentId, it.pageEndPoint)?.let { item ->
                item.getComponentsItem()?.find{ childItem ->
                    childItem.name == ComponentNames.QuickFilter.componentName
                }.apply {
                    this?.searchParameter?.getSearchParameterHashMap()?.clear()
                }
            }
            needReSyncMutableLiveData.value =  true
        }
        needReSyncMutableLiveData.value =  false
    }

}
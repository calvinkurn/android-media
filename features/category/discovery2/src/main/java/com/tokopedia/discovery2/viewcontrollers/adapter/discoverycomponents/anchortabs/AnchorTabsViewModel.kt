package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.anchortabs

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.discoverymapper.DiscoveryDataMapper
import com.tokopedia.discovery2.usecase.AnchorTabsUseCase
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class AnchorTabsViewModel(
    val application: Application,
    val components: ComponentsItem,
    val position: Int
) : DiscoveryBaseViewModel(), CoroutineScope {

    private val carouselList: MutableLiveData<List<ComponentsItem>> = MutableLiveData()
    fun getCarouselItemsListData(): LiveData<List<ComponentsItem>> = carouselList

    private val updatePositions: SingleLiveEvent<Boolean> = SingleLiveEvent()
    fun getUpdatePositionsLD(): LiveData<Boolean> = updatePositions

    private val _showMissingSectionToaster: SingleLiveEvent<Boolean> = SingleLiveEvent()
    fun shouldShowMissingSectionToaster(): LiveData<Boolean> = _showMissingSectionToaster

    private val sectionPositionMap: MutableMap<String, Int> = mutableMapOf()
    var pauseDispatchChanges = false
    var selectedSectionPos = Integer.MAX_VALUE
    var selectedSectionId = ""
    private var sectionDeleted = false

    @Inject
    lateinit var anchorTabsUseCase: AnchorTabsUseCase

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

    override fun onAttachToViewHolder() {
        super.onAttachToViewHolder()
        mapToComponents()
        setupData()
    }

    private fun setupData() {
        carouselList.value = components.getComponentsItem()
    }

    private fun mapToComponents() {
        if (components.noOfPagesLoaded != 1) {
            components.noOfPagesLoaded = 1
            components.data?.let {
                sectionPositionMap.clear()
                val compList = DiscoveryDataMapper.discoveryDataMapper.mapAnchorListToComponentList(
                    itemList = it,
                    subComponentName = ComponentNames.AnchorTabsItem.componentName,
                    parentComponentName = ComponentNames.AnchorTabs.componentName,
                    position = position,
                    compId = components.id,
                    anchorMap = sectionPositionMap
                )
                if(selectedSectionId.isEmpty() && compList.isNotEmpty()){
                    selectedSectionId = compList[0].data?.firstOrNull()?.targetSectionID?:""
                }
                if(anchorTabsUseCase.selectedId.isEmpty()){
                    anchorTabsUseCase.selectedId = selectedSectionId
                }
                components.setComponentsItem(compList)
            }
        }
    }

    private fun getPositionForSectionID(id: String): Int? {
        return sectionPositionMap[id]
    }

    fun updateSelectedSection(sectionId: String, isClickNotify: Boolean) {
        val newPos = getPositionForSectionID(sectionId) ?: selectedSectionPos
        if (newPos != selectedSectionPos && newPos >= 0 && newPos < getListSize()) {
            val newItem = components.getComponentsItem()?.get(newPos)
            newItem?.shouldRefreshComponent = true
            if(selectedSectionPos < getListSize()) {
                val oldItem = components.getComponentsItem()?.get(selectedSectionPos)
                oldItem?.shouldRefreshComponent = true
            }
            if (isClickNotify || !pauseDispatchChanges) {
                anchorTabsUseCase.selectedId = sectionId
                selectedSectionPos = newPos
                updatePositions.value = true
            }
            if (isClickNotify)
                pauseDispatchChanges = true
            selectedSectionId = sectionId
        }
    }

    fun deleteSectionTab(sectionID: String) {
        if (sectionPositionMap.containsKey(sectionID)) {
            var shouldSync = false
            val dataList = arrayListOf<DataItem>()
            if (!components.data.isNullOrEmpty()) {
                components.data?.forEach { dataItem ->
                    if (sectionID == dataItem.targetSectionID) {
                        if (sectionID == anchorTabsUseCase.selectedId) {
                            this.pauseDispatchChanges = false
                            _showMissingSectionToaster.value = true
                        }
                        shouldSync = true
                    } else {
                        dataList.add(dataItem)
                    }
                }
            }
            if (shouldSync) {
                components.data = dataList
                components.noOfPagesLoaded = 0
                sectionDeleted = true
                mapToComponents()
                setupData()
            }
        }
    }

    fun wasSectionDeleted():Boolean{
        val temp = sectionDeleted
        sectionDeleted = false
        return temp
    }

    fun getListSize(): Int {
        return components.getComponentsItem()?.size ?: 0
    }


}
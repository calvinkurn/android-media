package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.anchortabs

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.discoverymapper.DiscoveryDataMapper
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

class AnchorTabsViewModel(
    val application: Application,
    val components: ComponentsItem,
    val position: Int
) : DiscoveryBaseViewModel(), CoroutineScope {

    private val carouselList: MutableLiveData<List<ComponentsItem>> = MutableLiveData()
    fun getCarouselItemsListData(): LiveData<List<ComponentsItem>> = carouselList

    private val updatePositions: MutableLiveData<Pair<Int, Int>> = MutableLiveData()
    fun getUpdatePositionsLD(): LiveData<Pair<Int, Int>> = updatePositions

    private val sectionPositionMap: MutableMap<String, Int> = mutableMapOf()
    var selectedSectionPos = 0
    var selectedSectionId = ""
    private var sectionDeleted = false

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
                    anchorMap = sectionPositionMap,
                    selectedSectionID = selectedSectionId
                )
                components.setComponentsItem(compList)
            }
        }
    }

    private fun getPositionForSectionID(id: String): Int? {
        return sectionPositionMap[id]
    }

    fun updateSelectedSection(sectionId: String) {
        val newPos = getPositionForSectionID(sectionId) ?: selectedSectionPos
        if (newPos != selectedSectionPos && newPos >= 0 && newPos < getListSize()) {
            val newItem = components.getComponentsItem()?.get(newPos)
            newItem?.data?.firstOrNull()?.isSelected = true
            if(selectedSectionPos < getListSize()) {
                val oldItem = components.getComponentsItem()?.get(selectedSectionPos)
                oldItem?.data?.firstOrNull()?.isSelected = false
            }
            updatePositions.value = Pair(selectedSectionPos, newPos)
            selectedSectionPos = newPos
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
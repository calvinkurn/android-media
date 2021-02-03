package com.tokopedia.discovery2.usecase

import com.tokopedia.discovery2.datamapper.getComponent
import javax.inject.Inject


class ChipSelectionUseCase @Inject constructor() {

    fun onChipSelection(componentId: String, pageIdentifier: String, chipSelectionId: String): Boolean {
        val component = getComponent(componentId, pageIdentifier)
        val chipFilterItemData = getComponent(chipSelectionId, pageIdentifier)?.data?.firstOrNull()
        component?.let { item ->
            val parentComponent = getComponent(item.parentComponentId, pageIdentifier)
            parentComponent?.getComponentsItem()?.let {
                parentComponent.setComponentsItem(it.map { childComponent ->
                    chipFilterItemData?.targetComponent?.let { targetStringList ->
                        targetStringList.split(",").forEach { targetId ->
                            if (targetId == childComponent.id) {
                                childComponent.apply {
                                    setComponentsItem(null, component.tabName)
                                    noOfPagesLoaded = 0
                                }
                            }
                        }
                        childComponent
                    }
                    childComponent
                }, component.tabName)
                parentComponent.chipSelectionData = chipFilterItemData
            }
            return true
        }
        return false
    }

    fun onChipUnSelection(componentId: String, pageIdentifier: String): Boolean {
        val component = getComponent(componentId, pageIdentifier)
        component?.let {
            val parentComponent = getComponent(it.parentComponentId, pageIdentifier)
            parentComponent?.getComponentsItem()?.let {
                parentComponent.setComponentsItem(it.map { childComponent ->
                    if (childComponent.id != componentId) {
                        childComponent.apply {
                            setComponentsItem(null, component.tabName)
                            noOfPagesLoaded = 0
                        }
                    } else {
                        childComponent
                    }
                }, component.tabName)
                parentComponent.chipSelectionData = null
            }
            return true
        }

        return false
    }
}
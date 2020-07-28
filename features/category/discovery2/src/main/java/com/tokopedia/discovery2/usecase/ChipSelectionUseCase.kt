package com.tokopedia.discovery2.usecase

import com.tokopedia.discovery2.datamapper.getComponent
import javax.inject.Inject


class ChipSelectionUseCase @Inject constructor() {

    fun onChipSelection(componentId: String, pageIdentifier: String, chipSelectionId: String): Boolean {
        val component = getComponent(componentId, pageIdentifier)
        component?.let {
            val parentComponent = getComponent(it.parentComponentId, pageIdentifier)
            parentComponent?.getComponentsItem()?.let {
                parentComponent.setComponentsItem(it.map { childComponent ->
                    if (childComponent.id != componentId) {
                        childComponent.apply {
                            setComponentsItem(null)
                            noOfPagesLoaded = 0
                        }
                    } else {
                        childComponent
                    }

                })
                parentComponent.chipSelectionData = getComponent(chipSelectionId, pageIdentifier)?.data?.get(0)
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
                            setComponentsItem(null)
                            noOfPagesLoaded = 0
                        }
                    } else {
                        childComponent
                    }
                })
                parentComponent.chipSelectionData = null
            }
            return true
        }

        return false
    }
}
package com.tokopedia.discovery2.usecase

import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.HideSectionResponse
import com.tokopedia.discovery2.datamapper.getComponent
import javax.inject.Inject

class HideSectionUseCase @Inject constructor() {

    fun checkForHideSectionHandling(components: ComponentsItem): HideSectionResponse {
        if (!components.parentSectionCompID.isNullOrEmpty()) {
            getComponent(
                components.parentSectionCompID!!,
                components.pageEndPoint
            )?.let { parentSection ->
                parentSection.getComponentsItem()?.let { childList ->
                    if (childList.isNotEmpty()) {
                        if (childList.size == 1) {
                            return handleHideSection(parentSection, components.parentSectionId)
                        } else if (childList.size == 2) {
                            if (childList.any { componentsItem -> componentsItem.name == ComponentNames.LihatSemua.componentName }) {
                                return handleHideSection(parentSection, components.parentSectionId)
                            }
                        }
                    }
                }
            }
        }
        return HideSectionResponse(false, "")
    }

    private fun handleHideSection(
        parentSection: ComponentsItem,
        sectionId: String?
    ): HideSectionResponse {
        parentSection.setComponentsItem(arrayListOf())
        return HideSectionResponse(true, sectionId ?: "")
    }

}
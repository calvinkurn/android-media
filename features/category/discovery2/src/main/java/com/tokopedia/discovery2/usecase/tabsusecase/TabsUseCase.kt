package com.tokopedia.discovery2.usecase.tabsusecase

import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.repository.discoveryPage.DiscoveryPageRepository
import com.tokopedia.discovery2.repository.discoveryPage.DiscoveryPageRestRepository
import javax.inject.Inject

class TabsUseCase @Inject constructor() {
    lateinit var targetComponentIdList: List<String>
    lateinit var compositeComponentsList: List<ComponentsItem>
    private val discoveryPageData = DiscoveryPageRepository.discoveryResponseData.components
    fun getComponentsWithID(selectedTabData: DataItem): List<ComponentsItem> {

        if (!selectedTabData.targetComponentId.isNullOrEmpty()) {
            targetComponentIdList = selectedTabData.targetComponentId.split(",").map { it.trim() }
            if (!targetComponentIdList.isNullOrEmpty() && !discoveryPageData.isNullOrEmpty()) {
                targetComponentIdList.forEach { componentId ->
                    compositeComponentsList = discoveryPageData.filter { it.id == componentId }
                }

            }
        }
        return compositeComponentsList
    }
}

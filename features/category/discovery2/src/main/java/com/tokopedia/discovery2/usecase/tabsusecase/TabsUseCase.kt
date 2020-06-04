package com.tokopedia.discovery2.usecase.tabsusecase

import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.repository.discoveryPage.DiscoveryPageRestRepository
import java.util.ArrayList
import javax.inject.Inject

class TabsUseCase @Inject constructor() {
    lateinit var targetComponentIdList: List<String>
    lateinit var compositeComponentsList: ArrayList<ComponentsItem>
    private val discoveryPageData = DiscoveryPageRestRepository.discoveryResponseData.components
    fun getComponentsWithID(selectedTabData: DataItem): List<ComponentsItem> {

        if (!selectedTabData.targetComponentId.isNullOrEmpty()) {
            compositeComponentsList = ArrayList()
            targetComponentIdList = selectedTabData.targetComponentId.split(",").map { it.trim() }
            if (!targetComponentIdList.isNullOrEmpty() && !discoveryPageData.isNullOrEmpty()) {
                targetComponentIdList.forEach { componentId ->
                    compositeComponentsList.addAll(discoveryPageData.filter { it.id == componentId })
                }

            }
        }
        return compositeComponentsList
    }
}

package com.tokopedia.discovery2.usecase.tabsusecase

import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.discoverymapper.DiscoveryDataMapper
import com.tokopedia.discovery2.repository.tabs.TabsRepository
import javax.inject.Inject

class DynamicTabsUseCase @Inject constructor(private val tabsRepository: TabsRepository) {

    suspend fun getTabData(componentId: String, pageIdentifier: String): Boolean {
        val component = getComponent(componentId, pageIdentifier)
        component?.let {
            tabsRepository.getDynamicTabData(componentId, pageIdentifier).component?.let { dynamicTabData ->
                it.data = dynamicTabData.data
                it.setComponentsItem(DiscoveryDataMapper.mapTabsListToComponentList(dynamicTabData, ComponentNames.TabsItem.componentName))
            }
            return true
        }
        return false
    }
}

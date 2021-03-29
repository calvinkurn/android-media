package com.tokopedia.discovery2.usecase.tabsusecase

import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.discoverymapper.DiscoveryDataMapper
import com.tokopedia.discovery2.repository.tabs.TabsRepository
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import javax.inject.Inject

class DynamicTabsUseCase @Inject constructor(private val tabsRepository: TabsRepository) {
    private var shouldReSync: Boolean = false

    suspend fun getTabData(componentId: String, pageIdentifier: String): Boolean {
        val component = getComponent(componentId, pageIdentifier)
        component?.let {
            tabsRepository.getDynamicTabData(componentId, pageIdentifier).component?.let { dynamicTabData ->
                it.data = dynamicTabData.data
                it.properties  = dynamicTabData.properties
                it.setComponentsItem(DiscoveryDataMapper.mapTabsListToComponentList(dynamicTabData, ComponentNames.TabsItem.componentName), component.tabName)
            }
            return true
        }
        return false
    }

    fun updateTargetProductComponent(componentID: String, pageEndPoint: String) : Boolean{
        val tabComponent = getComponent(componentID, pageEndPoint)
            if (tabComponent?.getComponentsItem()?.size.isMoreThanZero()) {
                tabComponent?.getComponentsItem()?.forEach { item ->
                    checkComponent(item)
                }
            }
        return shouldReSync
    }

    private fun checkComponent(item: ComponentsItem) {
        if (item.getComponentsItem()?.size.isMoreThanZero()) {
            item.getComponentsItem()?.forEach { tabSubComps ->
                reInitialiseTabComponents(tabSubComps)
            }
        }
    }

    private fun reInitialiseTabComponents(componentsItem: ComponentsItem) {
        if (componentsItem.name == ComponentNames.ProductCardRevamp.componentName
                || componentsItem.name == ComponentNames.ProductCardSprintSale.componentName
                || componentsItem.name == ComponentNames.ProductCardCarousel.componentName
                || componentsItem.name == ComponentNames.ProductCardSprintSaleCarousel.componentName) {
            shouldReSync = true
            componentsItem.setComponentsItem(null)
            componentsItem.noOfPagesLoaded = 0
        }
    }
}

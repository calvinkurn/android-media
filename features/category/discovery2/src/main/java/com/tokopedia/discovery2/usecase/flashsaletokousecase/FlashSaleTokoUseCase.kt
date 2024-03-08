package com.tokopedia.discovery2.usecase.flashsaletokousecase

import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.discoverymapper.DiscoveryDataMapper
import com.tokopedia.discovery2.repository.flashsaletoko.FlashSaleTokoRepository
import javax.inject.Inject

class FlashSaleTokoUseCase @Inject constructor(private val repository: FlashSaleTokoRepository) {

    suspend fun getData(componentId: String, pageIdentifier: String): Boolean {
        val component = getComponent(componentId, pageIdentifier) ?: return false

        if (!component.parentSectionId.isNullOrEmpty()) return false

        repository.getTabData(componentId, pageIdentifier).component?.let { flashSaleTab ->
            component.data = flashSaleTab.data
            component.properties = flashSaleTab.properties
            component.setComponentsItem(
                DiscoveryDataMapper
                    .mapTabsListToComponentList(
                        flashSaleTab,
                        ComponentNames.FlashSaleTokoTab.componentName
                    ),
                component.name
            )

            return true
        }

        return false
    }
}

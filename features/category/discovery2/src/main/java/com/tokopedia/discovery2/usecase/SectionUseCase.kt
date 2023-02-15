package com.tokopedia.discovery2.usecase

import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.Constant
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.Utils.Companion.RPC_FILTER_KEY
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.datamapper.getCartData
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.datamapper.getMapWithRpc
import com.tokopedia.discovery2.datamapper.getMapWithoutRpc
import com.tokopedia.discovery2.discoverymapper.DiscoveryDataMapper
import com.tokopedia.discovery2.repository.section.SectionRepository
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SectionUseCase @Inject constructor(private val sectionRepository: SectionRepository) {

    suspend fun getChildComponents(componentId: String, pageEndPoint: String): Boolean {
        val component = getComponent(componentId, pageEndPoint)
        val paramWithoutRpc = getMapWithoutRpc(pageEndPoint)
        val paramWithRpc = getMapWithRpc(pageEndPoint)
        if (component?.noOfPagesLoaded == 1) return false
        component?.let {
            val components = sectionRepository.getComponents(
                pageEndPoint,
                it.sectionId,
                getQueryFilterString(
                    it.userAddressData,
                    paramWithoutRpc,
                    paramWithRpc,
                    component
                )
            )
            withContext(Dispatchers.IO) {
                components.forEach { comp ->
                    comp.parentSectionId = it.sectionId
                    comp.parentSectionCompID = it.id
                    val creativeName = comp.creativeName ?: ""
                    var isProductComponent = true
                    comp.pageEndPoint = component.pageEndPoint
                    comp.pagePath = component.pagePath
                    comp.tabPosition = it.tabPosition
                    val productListData = when (comp.name) {
                        ComponentNames.ProductCardRevamp.componentName -> {
                            if (comp.properties?.template == Constant.ProductTemplate.LIST) {
                                DiscoveryDataMapper().mapListToComponentList(
                                    comp.data,
                                    ComponentNames.MasterProductCardItemList.componentName,
                                    comp.properties,
                                    creativeName,
                                    parentSectionId = comp.parentSectionId
                                )
                            } else {
                                DiscoveryDataMapper().mapListToComponentList(
                                    comp.data,
                                    ComponentNames.ProductCardRevampItem.componentName,
                                    comp.properties,
                                    creativeName,
                                    parentSectionId = comp.parentSectionId
                                )
                            }
                        }
                        ComponentNames.ProductCardCarousel.componentName -> {
                            if (comp.properties?.template == Constant.ProductTemplate.LIST) {
                                DiscoveryDataMapper().mapListToComponentList(
                                    comp.data,
                                    ComponentNames.ProductCardCarouselItemList.componentName,
                                    comp.properties,
                                    creativeName,
                                    parentSectionId = comp.parentSectionId
                                )
                            } else {
                                DiscoveryDataMapper().mapListToComponentList(
                                    comp.data,
                                    ComponentNames.ProductCardCarouselItem.componentName,
                                    comp.properties,
                                    creativeName,
                                    parentSectionId = comp.parentSectionId
                                )
                            }
                        }
                        ComponentNames.ProductCardSprintSale.componentName -> {
                            DiscoveryDataMapper().mapListToComponentList(
                                comp.data,
                                ComponentNames.ProductCardSprintSaleItem.componentName,
                                comp.properties,
                                creativeName,
                                parentSectionId = comp.parentSectionId
                            )
                        }
                        ComponentNames.ProductCardSprintSaleCarousel.componentName -> {
                            DiscoveryDataMapper().mapListToComponentList(
                                comp.data,
                                ComponentNames.ProductCardSprintSaleCarouselItem.componentName,
                                comp.properties,
                                creativeName
                            )
                        }
                        else -> {
                            isProductComponent = false
                            null
                        }
                    }
                    if (isProductComponent) {
                        comp.nextPageKey = comp.compAdditionalInfo?.nextPage
                        comp.setComponentsItem(productListData, component.tabName)
                        comp.noOfPagesLoaded = 1
                        if (productListData?.isNotEmpty() == true) {
                            if (comp.properties?.tokonowATCActive == true) {
                                Utils.updateProductAddedInCart(
                                    productListData,
                                    getCartData(pageEndPoint)
                                )
                            }
                            comp.pageLoadedCounter = 2
                            comp.verticalProductFailState = false
                            comp.showVerticalLoader = true
                        } else {
                            comp.showVerticalLoader = false
                        }
                    }
                }
            }
            it.setComponentsItem(components, component.tabName)
            it.noOfPagesLoaded = 1
            it.verticalProductFailState = false
            return true
        }
        return false
    }

    private fun getQueryFilterString(
        userAddressData: LocalCacheModel?,
        queryParameterMapWithoutRpc: Map<String, String>?,
        queryParameterMapWithRpc: Map<String, String>?,
        sectionComponent: ComponentsItem
    ): String {
        val queryParameterMap = mutableMapOf<String, Any>()
        queryParameterMap.putAll(Utils.addAddressQueryMapWithWareHouse(userAddressData))
        queryParameterMapWithoutRpc?.let {
            queryParameterMap.putAll(it)
        }
        queryParameterMapWithRpc?.let {
            for (entry in it) {
                val adjustedValue = Utils.isRPCFilterApplicableForTab(entry.value, sectionComponent)
                if (adjustedValue.isNotEmpty())
                    queryParameterMap[RPC_FILTER_KEY + entry.key] = adjustedValue
            }
        }
        return Utils.getQueryString(queryParameterMap)
    }
}

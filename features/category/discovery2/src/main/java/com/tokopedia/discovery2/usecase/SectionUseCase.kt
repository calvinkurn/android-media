package com.tokopedia.discovery2.usecase

import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.Constant
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.datamapper.getCartData
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.discoverymapper.DiscoveryDataMapper
import com.tokopedia.discovery2.repository.section.SectionRepository
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SectionUseCase @Inject constructor(private val sectionRepository: SectionRepository) {

    suspend fun getChildComponents(componentId: String, pageEndPoint: String):Boolean{
        val component = getComponent(componentId, pageEndPoint)
        if (component?.noOfPagesLoaded == 1) return false
        component?.let {
            val components = sectionRepository.getComponents(pageEndPoint,it.sectionId,getQueryFilterString(
                it.userAddressData
            ))
            withContext(Dispatchers.IO){
                components.forEach { comp ->
                    comp.parentSectionId = it.sectionId
                    val creativeName = comp.creativeName ?: ""
                    var isProductComponent = true
                    val productListData = when(comp.name) {
                        ComponentNames.ProductCardRevamp.componentName -> {
                            if (comp.properties?.template == Constant.ProductTemplate.LIST) {
                                DiscoveryDataMapper().mapListToComponentList(
                                    comp.data,
                                    ComponentNames.MasterProductCardItemList.componentName,
                                    comp.properties,
                                    creativeName
                                )
                            } else {
                                DiscoveryDataMapper().mapListToComponentList(
                                    comp.data,
                                    ComponentNames.ProductCardRevampItem.componentName,
                                    comp.properties,
                                    creativeName
                                )
                            }
                        }
                        ComponentNames.ProductCardCarousel.componentName -> {
                            DiscoveryDataMapper().mapListToComponentList(
                                comp.data,
                                ComponentNames.ProductCardCarouselItem.componentName,
                                comp.properties,
                                creativeName
                            )
                        }
                        ComponentNames.ProductCardSprintSale.componentName -> {
                            DiscoveryDataMapper().mapListToComponentList(
                                comp.data,
                                ComponentNames.ProductCardSprintSaleItem.componentName,
                                comp.properties,
                                creativeName
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
                        else ->{
                            isProductComponent = false
                            null
                        }
                    }
                    if(isProductComponent){
                        comp.nextPageKey = comp.compAdditionalInfo?.nextPage
                        comp.setComponentsItem(productListData, component.tabName)
                        comp.noOfPagesLoaded = 1
                        if (productListData?.isNotEmpty() == true) {
                            if (comp.properties?.tokonowATCActive == true)
                                Utils.updateProductAddedInCart(productListData, getCartData(pageEndPoint))
                            comp.pageLoadedCounter = 2
                            comp.verticalProductFailState = false
                            comp.showVerticalLoader = true
                        }else{
                            comp.showVerticalLoader = false
                        }
                    }

                }
            }
            it.setComponentsItem(components)
            it.noOfPagesLoaded = 1
            return true
        }
        return false
    }

    private fun getQueryFilterString(userAddressData:LocalCacheModel?):String {
        val queryParameterMap = mutableMapOf<String, Any>()
        queryParameterMap.putAll(Utils.addAddressQueryMapWithWareHouse(userAddressData))
        return Utils.getQueryString(queryParameterMap)
    }

}
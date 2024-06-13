package com.tokopedia.discovery2.usecase.sectionusecase

import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.Constant
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.Utils.Companion.RPC_FILTER_KEY
import com.tokopedia.discovery2.Utils.Companion.isOldProductCardType
import com.tokopedia.discovery2.analytics.TrackingMapper.setAppLog
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.datamapper.getCartData
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.datamapper.getMapWithRpc
import com.tokopedia.discovery2.datamapper.getMapWithoutRpc
import com.tokopedia.discovery2.discoverymapper.DiscoveryDataMapper
import com.tokopedia.discovery2.repository.section.SectionRepository
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.productcard.experiments.ProductCardExperiment
import com.tokopedia.remoteconfig.RemoteConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SectionUseCase @Inject constructor(
    private val sectionRepository: SectionRepository,
    private val remoteConfig: RemoteConfig
) {

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
                    comp.data.apply {
                        setAppLog(comp.compAdditionalInfo?.tracker, comp.getSource())
                    }
                    val productListData = when (comp.name) {
                        ComponentNames.ProductCardRevamp.componentName -> {
                            if (comp.properties?.template == Constant.ProductTemplate.LIST) {
                                if (comp.properties.isOldProductCardType()) {
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
                                        ComponentNames.MasterProductCardItemListReimagine.componentName,
                                        comp.properties,
                                        creativeName,
                                        parentSectionId = comp.parentSectionId
                                    )
                                }
                            } else {
                                if (comp.properties.isOldProductCardType()) {
                                    DiscoveryDataMapper().mapListToComponentList(
                                        comp.data,
                                        ComponentNames.ProductCardRevampItem.componentName,
                                        comp.properties,
                                        creativeName,
                                        parentSectionId = comp.parentSectionId
                                    )
                                } else {
                                    DiscoveryDataMapper().mapListToComponentList(
                                        comp.data,
                                        ComponentNames.MasterProductCardItemReimagine.componentName,
                                        comp.properties,
                                        creativeName,
                                        parentSectionId = comp.parentSectionId
                                    )
                                }
                            }
                        }

                        ComponentNames.ProductCardCarousel.componentName -> {
                            if (comp.properties?.template == Constant.ProductTemplate.LIST) {
                                if (comp.properties.isOldProductCardType()) {
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
                                        ComponentNames.ProductCardCarouselItemListReimagine.componentName,
                                        comp.properties,
                                        creativeName,
                                        parentSectionId = comp.parentSectionId
                                    )
                                }
                            } else {
                                if (comp.properties.isOldProductCardType()) {
                                    DiscoveryDataMapper().mapListToComponentList(
                                        comp.data,
                                        ComponentNames.ProductCardCarouselItem.componentName,
                                        comp.properties,
                                        creativeName,
                                        parentSectionId = comp.parentSectionId
                                    )
                                } else {
                                    DiscoveryDataMapper().mapListToComponentList(
                                        comp.data,
                                        ComponentNames.ProductCardCarouselItemReimagine.componentName,
                                        comp.properties,
                                        creativeName,
                                        parentSectionId = comp.parentSectionId
                                    )
                                }
                            }
                        }

                        ComponentNames.ProductCardSprintSale.componentName -> {
                            if (comp.properties.isOldProductCardType()) {
                                DiscoveryDataMapper().mapListToComponentList(
                                    comp.data,
                                    ComponentNames.ProductCardSprintSaleItem.componentName,
                                    comp.properties,
                                    creativeName,
                                    parentSectionId = comp.parentSectionId
                                )
                            } else {
                                DiscoveryDataMapper().mapListToComponentList(
                                    comp.data,
                                    ComponentNames.ProductCardSprintSaleItemReimagine.componentName,
                                    comp.properties,
                                    creativeName,
                                    parentSectionId = comp.parentSectionId
                                )
                            }
                        }

                        ComponentNames.ProductCardSprintSaleCarousel.componentName -> {
                            if (comp.properties.isOldProductCardType()) {
                                DiscoveryDataMapper().mapListToComponentList(
                                    comp.data,
                                    ComponentNames.ProductCardSprintSaleCarouselItem.componentName,
                                    comp.properties,
                                    creativeName
                                )
                            } else {
                                DiscoveryDataMapper().mapListToComponentList(
                                    comp.data,
                                    ComponentNames.ProductCardSprintSaleCarouselItemReimagine.componentName,
                                    comp.properties,
                                    creativeName
                                )
                            }
                        }

                        ComponentNames.ContentCard.componentName -> {
                            DiscoveryDataMapper().mapListToComponentList(
                                comp.data,
                                ComponentNames.ContentCardItem.componentName,
                                comp.properties,
                                creativeName,
                                parentSectionId = comp.parentSectionId
                            )
                        }

                        ComponentNames.ShopOfferHeroBrand.componentName -> {
                            val subComponentName = if (comp.properties.isOldProductCardType()) {
                                ComponentNames.ShopOfferHeroBrandProductItem.componentName
                            } else {
                                ComponentNames.ShopOfferHeroBrandProductItemReimagine.componentName
                            }

                            DiscoveryDataMapper().mapListToComponentList(
                                comp.data,
                                subComponentName,
                                comp.properties,
                                creativeName,
                                parentSectionId = comp.parentSectionId
                            )
                        }

                        ComponentNames.CalendarWidgetCarousel.componentName -> {
                            DiscoveryDataMapper().mapListToComponentList(
                                comp.data,
                                ComponentNames.CalendarWidgetItem.componentName,
                                comp.properties,
                                creativeName,
                                parentSectionId = comp.parentSectionId
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

            val isBackgroundAvailable = it.properties?.run {
                !(backgroundImageUrl.isNullOrEmpty() && foregroundImageUrl.isNullOrEmpty())
            } ?: false

            applyFestiveBackground(isBackgroundAvailable, components, it.position)

            it.setComponentsItem(components, component.tabName)
            it.noOfPagesLoaded = 1
            it.verticalProductFailState = false
            return true
        }
        return false
    }

    private fun applyFestiveBackground(
        isBackgroundAvailable: Boolean,
        components: List<ComponentsItem>,
        sectionPosition: Int
    ) {
        val isFestiveBackgroundEnable = remoteConfig.getBoolean(SECTION_FESTIVE_BACKGROUND_TOGGLE)

        val policy = FestiveEligibilityPolicy(isFestiveBackgroundEnable, isBackgroundAvailable)
        val componentNames = components.map { it.name }

        if (policy.isAllowed(componentNames)) {
            val offsetPosition = 1
            components.forEachIndexed { index, item ->
                item.apply {
                    isBackgroundPresent = true
                    position = sectionPosition + offsetPosition + index
                }
            }
        }
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
                if (adjustedValue.isNotEmpty()) {
                    queryParameterMap[RPC_FILTER_KEY + entry.key] = adjustedValue
                }
            }
        }
        queryParameterMap[Utils.SRE_IDENTIFIER] = Utils.SRE_VALUE
        return Utils.getQueryString(queryParameterMap)
    }

    companion object {
        private const val SECTION_FESTIVE_BACKGROUND_TOGGLE =
            "android_main_app_enable_disco_section_background"
    }
}

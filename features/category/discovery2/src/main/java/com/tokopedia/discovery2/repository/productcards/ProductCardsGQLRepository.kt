package com.tokopedia.discovery2.repository.productcards

import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.Constant
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.data.ComponentAdditionalInfo
import com.tokopedia.discovery2.data.ComponentSourceData
import com.tokopedia.discovery2.data.ComponentTracker
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.data.DataResponse
import com.tokopedia.discovery2.data.TopLevelTab
import com.tokopedia.discovery2.data.UnknownTab
import com.tokopedia.discovery2.data.gqlraw.GQL_COMPONENT
import com.tokopedia.discovery2.data.gqlraw.GQL_COMPONENT_QUERY_NAME
import com.tokopedia.discovery2.data.productcarditem.ProductCardRequest
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.discoverymapper.DiscoveryDataMapper
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.masterproductcarditem.MasterProductCardItemViewHolder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProductCardsGQLRepository @Inject constructor() : BaseRepository(), ProductCardsRepository {
    override suspend fun getProducts(
        requestParams: ProductCardRequest,
        queryParameterMap: MutableMap<String, Any>
    ): Pair<ArrayList<ComponentsItem>, ComponentAdditionalInfo?> {
        val response = (
            getGQLData(
                GQL_COMPONENT,
                DataResponse::class.java,
                Utils.getComponentsGQLParams(
                    requestParams.componentId,
                    requestParams.pageEndpoint,
                    Utils.getQueryString(queryParameterMap),
                    requestParams.sessionId
                ),
                GQL_COMPONENT_QUERY_NAME
            ) as DataResponse
            )

        val componentProperties = response.data.component?.properties
        val creativeName = response.data.component?.creativeName ?: ""
        val additionalInfo = response.data.component?.compAdditionalInfo
        val componentItem = getComponent(requestParams.componentId, requestParams.pageEndpoint)

        val componentData = response.data.component?.let {
            it.data.apply {
                setAppLog(additionalInfo?.tracker, it.getSource())
                setTopLevelTab(it)
            }
        }

        val componentsListSize = componentItem?.getComponentsItem()?.size ?: 0
        val list = withContext(Dispatchers.Default) {
            when (requestParams.componentName) {
                ComponentNames.ProductCardColumnList.componentName -> {
                    DiscoveryDataMapper().mapListToComponentList(
                        componentData,
                        ComponentNames.ProductCardColumnList.componentName,
                        componentProperties,
                        creativeName,
                        parentListSize = componentsListSize,
                        parentSectionId = componentItem?.parentSectionId
                    )
                }

                ComponentNames.ProductCardRevamp.componentName -> {
                    if (componentProperties?.template == Constant.ProductTemplate.LIST) {
                        if (componentProperties.cardType.equals("V1", true)) {
                            DiscoveryDataMapper().mapListToComponentList(
                                componentData,
                                ComponentNames.MasterProductCardItemList.componentName,
                                componentProperties,
                                creativeName,
                                parentListSize = componentsListSize,
                                parentSectionId = componentItem?.parentSectionId
                            )
                        } else {
                            DiscoveryDataMapper().mapListToComponentList(
                                componentData,
                                ComponentNames.MasterProductCardItemListReimagine.componentName,
                                componentProperties,
                                creativeName,
                                parentListSize = componentsListSize,
                                parentSectionId = componentItem?.parentSectionId
                            )
                        }
                    } else {
                        if (componentProperties?.cardType.equals("V1", true)) {
                            DiscoveryDataMapper().mapListToComponentList(
                                componentData,
                                ComponentNames.ProductCardRevampItem.componentName,
                                componentProperties,
                                creativeName,
                                parentListSize = componentsListSize,
                                parentSectionId = componentItem?.parentSectionId
                            )
                        } else {
                            DiscoveryDataMapper().mapListToComponentList(
                                componentData,
                                ComponentNames.MasterProductCardItemReimagine.componentName,
                                componentProperties,
                                creativeName,
                                parentListSize = componentsListSize,
                                parentSectionId = componentItem?.parentSectionId
                            )
                        }
                    }
                }

                ComponentNames.ProductCardSingle.componentName -> {
                    if (componentProperties?.cardType.equals("V1", true)) {
                        DiscoveryDataMapper().mapListToComponentList(
                            componentData,
                            ComponentNames.ProductCardSingleItem.componentName,
                            componentProperties,
                            creativeName,
                            parentListSize = componentsListSize,
                            parentSectionId = componentItem?.parentSectionId
                        )
                    } else {
                        DiscoveryDataMapper().mapListToComponentList(
                            componentData,
                            ComponentNames.ProductCardSingleItemReimagine.componentName,
                            componentProperties,
                            creativeName,
                            parentListSize = componentsListSize,
                            parentSectionId = componentItem?.parentSectionId
                        )
                    }
                }

                ComponentNames.ProductCardCarousel.componentName -> {
                    if (componentProperties?.template == Constant.ProductTemplate.LIST) {
                        if (componentProperties.cardType.equals("V1", true)) {
                            DiscoveryDataMapper().mapListToComponentList(
                                componentData,
                                ComponentNames.ProductCardCarouselItemList.componentName,
                                componentProperties,
                                creativeName,
                                parentListSize = componentsListSize
                            )
                        } else {
                            DiscoveryDataMapper().mapListToComponentList(
                                componentData,
                                ComponentNames.ProductCardCarouselItemListReimagine.componentName,
                                componentProperties,
                                creativeName,
                                parentListSize = componentsListSize
                            )
                        }
                    } else {
                        if (componentProperties?.cardType.equals("V1", true)) {
                            DiscoveryDataMapper().mapListToComponentList(
                                componentData,
                                ComponentNames.ProductCardCarouselItem.componentName,
                                componentProperties,
                                creativeName,
                                parentListSize = componentsListSize
                            )
                        } else {
                            DiscoveryDataMapper().mapListToComponentList(
                                componentData,
                                ComponentNames.ProductCardCarouselItemReimagine.componentName,
                                componentProperties,
                                creativeName,
                                parentListSize = componentsListSize
                            )
                        }
                    }
                }

                ComponentNames.ProductCardSprintSale.componentName -> {
                    if (componentProperties?.cardType.equals("V1", true)) {
                        DiscoveryDataMapper().mapListToComponentList(
                            componentData,
                            ComponentNames.ProductCardSprintSaleItem.componentName,
                            componentProperties,
                            creativeName,
                            parentListSize = componentsListSize,
                            parentSectionId = componentItem?.parentSectionId
                        )
                    } else {
                        DiscoveryDataMapper().mapListToComponentList(
                            componentData,
                            ComponentNames.ProductCardSprintSaleItemReimagine.componentName,
                            componentProperties,
                            creativeName,
                            parentListSize = componentsListSize,
                            parentSectionId = componentItem?.parentSectionId
                        )
                    }
                }

                ComponentNames.ProductCardSprintSaleCarousel.componentName -> {
                    if (componentProperties?.cardType.equals("V1", true)) {
                        DiscoveryDataMapper().mapListToComponentList(
                            componentData,
                            ComponentNames.ProductCardSprintSaleCarouselItem.componentName,
                            componentProperties,
                            creativeName,
                            parentListSize = componentsListSize
                        )
                    } else {
                        DiscoveryDataMapper().mapListToComponentList(
                            componentData,
                            ComponentNames.ProductCardSprintSaleCarouselItemReimagine.componentName,
                            componentProperties,
                            creativeName,
                            parentListSize = componentsListSize
                        )
                    }
                }

                ComponentNames.CalendarWidgetGrid.componentName, ComponentNames.CalendarWidgetCarousel.componentName ->
                    DiscoveryDataMapper().mapListToComponentList(
                        componentData,
                        ComponentNames.CalendarWidgetItem.componentName,
                        componentProperties,
                        creativeName,
                        parentComponentPosition = componentItem?.position,
                        parentListSize = componentsListSize,
                        parentSectionId = componentItem?.parentSectionId
                    )

                ComponentNames.ShopOfferHeroBrand.componentName -> DiscoveryDataMapper().mapListToComponentList(
                    itemList = componentData,
                    subComponentName = ComponentNames.ShopOfferHeroBrandProductItem.componentName,
                    properties = componentProperties,
                    creativeName = creativeName,
                    parentListSize = componentsListSize
                )

                else ->
                    DiscoveryDataMapper().mapListToComponentList(
                        componentData,
                        ComponentNames.ProductCardRevampItem.componentName,
                        null,
                        creativeName,
                        parentListSize = componentsListSize
                    )
            }
        }
        return Pair(list, additionalInfo)
    }

    private fun List<DataItem>?.setAppLog(
        tracker: ComponentTracker?,
        source: ComponentSourceData,
    ) {
        tracker?.let { componentTracker ->
            this?.forEachIndexed { index, dataItem ->
                dataItem.itemPosition = index
                dataItem.setAppLog(componentTracker)
                dataItem.source = source
            }
        }
    }

    private fun List<DataItem>?.setTopLevelTab(componentsItem: ComponentsItem) {
        this?.forEach { dataItem ->
            dataItem.topLevelTab = getTopLevelParentComponent(componentsItem)
        }
    }

    private fun getTopLevelParentComponent(component: ComponentsItem?): TopLevelTab {
        if (component == null) return UnknownTab

        val parentComponentId = component.parentComponentId
        if (parentComponentId.isEmpty()) return component.getSelectedTab()

        val parentComponent = getComponent(parentComponentId, component.pageEndPoint)
        return getTopLevelParentComponent(parentComponent)
    }

    private fun ComponentsItem.getSelectedTab(): TopLevelTab {
        return data?.let { dataItem ->
            val index = dataItem.indexOfFirst { it.isSelected }

            if (index < 0) return UnknownTab

            return TopLevelTab(dataItem[index].name.orEmpty(), index)
        } ?: UnknownTab
    }
}

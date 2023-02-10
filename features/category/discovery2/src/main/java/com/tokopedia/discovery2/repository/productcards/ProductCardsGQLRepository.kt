package com.tokopedia.discovery2.repository.productcards

import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.Constant
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.data.ComponentAdditionalInfo
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataResponse
import com.tokopedia.discovery2.data.gqlraw.GQL_COMPONENT
import com.tokopedia.discovery2.data.gqlraw.GQL_COMPONENT_QUERY_NAME
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.discoverymapper.DiscoveryDataMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProductCardsGQLRepository @Inject constructor() : BaseRepository(), ProductCardsRepository {
    override suspend fun getProducts(componentId: String, queryParamterMap: MutableMap<String, Any>, pageEndPoint: String, productComponentName: String?): Pair<ArrayList<ComponentsItem>, ComponentAdditionalInfo?>{
        val response = (getGQLData(GQL_COMPONENT,
                DataResponse::class.java, Utils.getComponentsGQLParams(componentId, pageEndPoint, Utils.getQueryString(queryParamterMap)), GQL_COMPONENT_QUERY_NAME) as DataResponse)

        val componentData = response.data.component?.data
        val componentProperties = response.data.component?.properties
        val creativeName = response.data.component?.creativeName ?: ""
        val additionalInfo = response.data.component?.compAdditionalInfo
        val componentItem  = getComponent(componentId, pageEndPoint)
        val componentsListSize = componentItem?.getComponentsItem()?.size ?: 0
        val list = withContext(Dispatchers.Default) {
            when (productComponentName) {
            ComponentNames.ProductCardRevamp.componentName -> {
                if (componentProperties?.template == Constant.ProductTemplate.LIST) {
                    DiscoveryDataMapper().mapListToComponentList(componentData, ComponentNames.MasterProductCardItemList.componentName, componentProperties, creativeName, parentListSize = componentsListSize, parentSectionId = componentItem?.parentSectionId)
                } else {
                    DiscoveryDataMapper().mapListToComponentList(componentData, ComponentNames.ProductCardRevampItem.componentName, componentProperties, creativeName, parentListSize = componentsListSize, parentSectionId = componentItem?.parentSectionId)
                }
            }
            ComponentNames.ProductCardSingle.componentName -> {
                DiscoveryDataMapper().mapListToComponentList(componentData, ComponentNames.ProductCardSingleItem.componentName, componentProperties, creativeName, parentListSize = componentsListSize, parentSectionId = componentItem?.parentSectionId)
            }
            ComponentNames.ProductCardCarousel.componentName ->
                DiscoveryDataMapper().mapListToComponentList(componentData, ComponentNames.ProductCardCarouselItem.componentName, componentProperties, creativeName, parentListSize = componentsListSize)
            ComponentNames.ProductCardSprintSale.componentName ->
                DiscoveryDataMapper().mapListToComponentList(componentData, ComponentNames.ProductCardSprintSaleItem.componentName, componentProperties, creativeName, parentListSize = componentsListSize, parentSectionId = componentItem?.parentSectionId)
            ComponentNames.ProductCardSprintSaleCarousel.componentName ->
                DiscoveryDataMapper().mapListToComponentList(componentData, ComponentNames.ProductCardSprintSaleCarouselItem.componentName, componentProperties, creativeName, parentListSize = componentsListSize)
            ComponentNames.CalendarWidgetGrid.componentName, ComponentNames.CalendarWidgetCarousel.componentName ->
                DiscoveryDataMapper().mapListToComponentList(componentData, ComponentNames.CalendarWidgetItem.componentName, componentProperties, creativeName,
                    parentComponentPosition = componentItem?.position, parentListSize = componentsListSize, parentSectionId = componentItem?.parentSectionId)
            else ->
                DiscoveryDataMapper().mapListToComponentList(componentData, ComponentNames.ProductCardRevampItem.componentName, null, creativeName, parentListSize = componentsListSize)

            }
        }
        return Pair(list,additionalInfo)
    }
}

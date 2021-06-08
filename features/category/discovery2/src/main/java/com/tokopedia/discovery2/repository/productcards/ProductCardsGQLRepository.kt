package com.tokopedia.discovery2.repository.productcards

import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.Constant
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataResponse
import com.tokopedia.discovery2.data.gqlraw.GQL_COMPONENT
import com.tokopedia.discovery2.data.gqlraw.GQL_COMPONENT_QUERY_NAME
import com.tokopedia.discovery2.discoverymapper.DiscoveryDataMapper
import javax.inject.Inject

class ProductCardsGQLRepository @Inject constructor() : BaseRepository(), ProductCardsRepository {
    override suspend fun getProducts(componentId: String, queryParamterMap: MutableMap<String, Any>, pageEndPoint: String, productComponentName: String?): ArrayList<ComponentsItem> {
        val response = (getGQLData(GQL_COMPONENT,
                DataResponse::class.java, Utils.getComponentsGQLParams(componentId, pageEndPoint, Utils.getQueryString(queryParamterMap)), GQL_COMPONENT_QUERY_NAME) as DataResponse)

        val componentData = response.data.component?.data
        val componentProperties = response.data.component?.properties
        val creativeName = response.data.component?.creativeName ?: ""
        return when (productComponentName) {
            ComponentNames.ProductCardRevamp.componentName -> {
                if (componentProperties?.template == Constant.ProductTemplate.LIST) {
                    DiscoveryDataMapper().mapListToComponentList(componentData, ComponentNames.MasterProductCardItemList.componentName, componentProperties, creativeName)
                } else {
                    DiscoveryDataMapper().mapListToComponentList(componentData, ComponentNames.ProductCardRevampItem.componentName, componentProperties, creativeName)
                }
            }
            ComponentNames.ProductCardCarousel.componentName ->
                DiscoveryDataMapper().mapListToComponentList(componentData, ComponentNames.ProductCardCarouselItem.componentName, componentProperties, creativeName)
            ComponentNames.ProductCardSprintSale.componentName ->
                DiscoveryDataMapper().mapListToComponentList(componentData, ComponentNames.ProductCardSprintSaleItem.componentName, componentProperties, creativeName)
            ComponentNames.ProductCardSprintSaleCarousel.componentName ->
                DiscoveryDataMapper().mapListToComponentList(componentData, ComponentNames.ProductCardSprintSaleCarouselItem.componentName, componentProperties, creativeName)
            else ->
                DiscoveryDataMapper().mapListToComponentList(componentData, ComponentNames.ProductCardRevampItem.componentName, null, creativeName)

        }
    }
}
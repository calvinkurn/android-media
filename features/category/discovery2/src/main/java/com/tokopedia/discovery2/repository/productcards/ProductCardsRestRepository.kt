package com.tokopedia.discovery2.repository.productcards

import com.google.gson.reflect.TypeToken
import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.GenerateUrl
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DiscoveryResponse
import com.tokopedia.discovery2.discoverymapper.DiscoveryDataMapper
import com.tokopedia.network.data.model.response.DataResponse
import javax.inject.Inject

class ProductCardsRestRepository @Inject constructor() : BaseRepository(), ProductCardsRepository {

    override suspend fun getProducts(componentId: String, queryParamterMap: MutableMap<String, Any>, pageEndPoint: String, productComponentName: String?): ArrayList<ComponentsItem> {
        val response = getRestData<DataResponse<DiscoveryResponse>>(GenerateUrl.getComponentUrl(pageEndPoint, componentId),
                object : TypeToken<DataResponse<DiscoveryResponse>>() {}.type,
                RequestType.GET,
                queryParamterMap)

        val componentData = response.data.component?.data
        val componentProperties = response.data.component?.properties
        return when (productComponentName) {
            ComponentNames.ProductCardRevamp.componentName ->
                DiscoveryDataMapper().mapListToComponentList(componentData, ComponentNames.ProductCardRevampItem.componentName, componentProperties)
            ComponentNames.ProductCardCarousel.componentName ->
                DiscoveryDataMapper().mapListToComponentList(componentData, ComponentNames.ProductCardCarouselItem.componentName, componentProperties)
            ComponentNames.ProductCardSprintSale.componentName ->
                DiscoveryDataMapper().mapListToComponentList(componentData, ComponentNames.ProductCardSprintSaleItem.componentName, componentProperties)
            ComponentNames.ProductCardSprintSaleCarousel.componentName ->
                DiscoveryDataMapper().mapListToComponentList(componentData, ComponentNames.ProductCardSprintSaleCarouselItem.componentName, componentProperties)
            else ->
                DiscoveryDataMapper().mapListToComponentList(componentData, ComponentNames.ProductCardRevampItem.componentName, null)

        }
    }
}
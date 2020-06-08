package com.tokopedia.discovery2.repository.productcards

import com.google.gson.reflect.TypeToken
import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.common.network.data.model.RequestType
import com.tokopedia.discovery2.GenerateUrl
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DiscoveryResponse
import com.tokopedia.discovery2.discoverymapper.DiscoveryDataMapper
import com.tokopedia.network.data.model.response.DataResponse
import javax.inject.Inject

class ProductCardsRestRepository @Inject constructor() : BaseRepository(), ProductCardsRepository {

    override suspend fun  getProducts(componentId: String, queryParamterMap: MutableMap<String, Any>, pageEndPoint: String, productComponentName: String?): ArrayList<ComponentsItem> {
        val response = getRestData<DataResponse<DiscoveryResponse>>(GenerateUrl.getComponentUrl(pageEndPoint, componentId),
                object : TypeToken<DataResponse<DiscoveryResponse>>() {}.type,
                RequestType.GET,
                queryParamterMap)

        val componentData = response.data.component?.data
        val componentProperties = response.data.component?.properties
        return when (productComponentName) {
            "product_card_revamp" -> DiscoveryDataMapper().mapListToComponentList(componentData, "product_card_revamp_item", componentProperties)
            "product_card_carousel" -> DiscoveryDataMapper().mapListToComponentList(componentData, "product_card_carousel_item", componentProperties)
            "product_card_sprint_sale" -> DiscoveryDataMapper().mapListToComponentList(componentData, "product_card_sprint_sale_item", componentProperties)
            "product_card_sprint_sale_carousel" -> DiscoveryDataMapper().mapListToComponentList(componentData, "product_card_sprint_sale_carousel_item", componentProperties)
            else -> DiscoveryDataMapper().mapListToComponentList(componentData, "product_card_revamp_item", null)

        }
    }
}
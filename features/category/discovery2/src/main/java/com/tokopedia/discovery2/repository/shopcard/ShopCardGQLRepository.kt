package com.tokopedia.discovery2.repository.shopcard

import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataResponse
import com.tokopedia.discovery2.data.gqlraw.GQL_COMPONENT
import com.tokopedia.discovery2.data.gqlraw.GQL_COMPONENT_QUERY_NAME
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.discoverymapper.DiscoveryDataMapper
import javax.inject.Inject

class ShopCardGQLRepository @Inject constructor() : BaseRepository(), ShopCardRepository {
    override suspend fun getShopCardData(componentId: String, queryParamterMap: MutableMap<String, Any>, pageEndPoint: String, shopCardComponentName: String?): Pair<ArrayList<ComponentsItem>, String?> {
        val response = (getGQLData(GQL_COMPONENT,
                DataResponse::class.java, Utils.getComponentsGQLParams(componentId, pageEndPoint, Utils.getQueryString(queryParamterMap)), GQL_COMPONENT_QUERY_NAME) as DataResponse)

        val componentData = response.data.component?.data
        val componentProperties = response.data.component?.properties
        val creativeName = response.data.component?.creativeName ?: ""
        val nextPage = response.data.component?.compAdditionalInfo?.nextPage
        val componentsItem = getComponent(componentId,pageEndPoint)
        val componentsListSize = componentsItem?.getComponentsItem()?.size ?: 0
        val list = when (shopCardComponentName) {
            ComponentNames.ShopCardView.componentName -> {
                DiscoveryDataMapper().mapListToComponentList(componentData, ComponentNames.ShopCardItemView.componentName, componentProperties, creativeName, parentListSize = componentsListSize)
            }
            else ->
                DiscoveryDataMapper().mapListToComponentList(componentData, ComponentNames.ShopCardItemView.componentName, null, creativeName, parentListSize = componentsListSize)

        }
        return Pair(list, nextPage)
    }
}
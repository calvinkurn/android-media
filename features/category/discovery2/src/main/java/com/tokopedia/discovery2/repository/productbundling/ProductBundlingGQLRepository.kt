package com.tokopedia.discovery2.repository.productbundling

import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataResponse
import com.tokopedia.discovery2.data.gqlraw.GQL_COMPONENT
import com.tokopedia.discovery2.data.gqlraw.GQL_COMPONENT_QUERY_NAME
import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.discoverymapper.DiscoveryDataMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProductBundlingGQLRepository @Inject constructor() : BaseRepository(), ProductBundlingRepository {
    override suspend fun getProductBundlingData(
            componentId: String,
            queryParamterMap: MutableMap<String, Any>,
            pageEndPoint: String,
            productBundlingComponentName: String?
    ): ArrayList<ComponentsItem> {
        val response = (getGQLData(
                GQL_COMPONENT,
                DataResponse::class.java, Utils.getComponentsGQLParams(componentId, pageEndPoint, Utils.getQueryString(queryParamterMap)), GQL_COMPONENT_QUERY_NAME
        ) as DataResponse)

        val componentData = response.data.component?.data
        val componentProperties = response.data.component?.properties
        val creativeName = response.data.component?.creativeName ?: ""
        val parentComponentName = response.data.component?.name
        val componentsItem = getComponent(componentId, pageEndPoint)
        val componentsListSize = componentsItem?.getComponentsItem()?.size ?: 0
        val list = withContext(Dispatchers.Default) {
            when (productBundlingComponentName) {
                ComponentNames.ProductBundling.componentName -> {
                    DiscoveryDataMapper().mapListToComponentList(componentData, ComponentNames.ProductBundling.componentName, componentProperties, creativeName, parentListSize = componentsListSize, parentComponentName = parentComponentName)
                }
                else ->
                    DiscoveryDataMapper().mapListToComponentList(componentData, ComponentNames.ProductBundling.componentName, componentProperties, creativeName, parentListSize = componentsListSize, parentComponentName = parentComponentName)
            }
        }
        return list
    }
}
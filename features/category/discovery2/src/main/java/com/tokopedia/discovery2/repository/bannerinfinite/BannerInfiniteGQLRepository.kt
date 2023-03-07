package com.tokopedia.discovery2.repository.bannerinfinite

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

class BannerInfiniteGQLRepository @Inject constructor() : BaseRepository(), BannerInfiniteRepository {
    override suspend fun getBanners(componentId: String, queryParamterMap: MutableMap<String, Any>, pageEndPoint: String, bannerComponentName: String?): Pair<ArrayList<ComponentsItem>,String?>{
        val response = (getGQLData(GQL_COMPONENT,
                DataResponse::class.java, Utils.getComponentsGQLParams(componentId, pageEndPoint, Utils.getQueryString(queryParamterMap)), GQL_COMPONENT_QUERY_NAME) as DataResponse)

        val componentData = response.data.component?.data
        val componentProperties = response.data.component?.properties
        val nextPage = response.data.component?.compAdditionalInfo?.nextPage
        val componentItem  = getComponent(componentId, pageEndPoint)
        val componentsListSize = componentItem?.getComponentsItem()?.size ?: 0
        val list = when (bannerComponentName) {
            ComponentNames.BannerInfinite.componentName ->
                DiscoveryDataMapper().mapListToBannerComponentList(componentData, ComponentNames.BannerInfiniteItem.componentName, componentProperties, parentListSize = componentsListSize,parentSectionId = componentItem?.parentSectionId)
            else ->
                DiscoveryDataMapper().mapListToBannerComponentList(componentData, ComponentNames.BannerInfiniteItem.componentName, null, parentListSize = componentsListSize,parentSectionId = componentItem?.parentSectionId)

        }
        return Pair(list,nextPage)
    }
}
package com.tokopedia.discovery2.repository.contentCard

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

class ContentCardGQLRepository @Inject constructor() : BaseRepository(), ContentCardRepository {
    override suspend fun getContentCards(
        componentId: String,
        queryParamterMap: MutableMap<String, Any>,
        pageEndPoint: String,
        contentCardComponentName: String?
    ): Pair<ArrayList<ComponentsItem>, String?> {
        val response = (getGQLData(
            GQL_COMPONENT,
            DataResponse::class.java,
            Utils.getComponentsGQLParams(
                componentId,
                pageEndPoint,
                Utils.getQueryString(queryParamterMap)
            ),
            GQL_COMPONENT_QUERY_NAME
        ) as DataResponse)

        val component = response.data.component

        val componentData = component?.data
        val componentProperties = component?.properties
        val nextPage = component?.compAdditionalInfo?.nextPage
        val componentItem = getComponent(componentId, pageEndPoint)
        val componentsListSize = componentItem?.getComponentsItem()?.size ?: 0
        val parentPosition = componentItem?.position ?: 0
        val list = when (contentCardComponentName) {
            ComponentNames.ContentCard.componentName ->
                DiscoveryDataMapper().mapListToComponentList(
                    componentData,
                    ComponentNames.ContentCardItem.componentName,
                    componentProperties,
                    parentListSize = componentsListSize,
                    parentSectionId = componentItem?.parentSectionId,
                    parentComponentPosition = parentPosition
                )
            else ->
                DiscoveryDataMapper().mapListToComponentList(
                    componentData,
                    ComponentNames.ContentCardItem.componentName,
                    null,
                    parentListSize = componentsListSize,
                    parentSectionId = componentItem?.parentSectionId,
                    parentComponentPosition = parentPosition
                )

        }
        return Pair(list, nextPage)
    }
}

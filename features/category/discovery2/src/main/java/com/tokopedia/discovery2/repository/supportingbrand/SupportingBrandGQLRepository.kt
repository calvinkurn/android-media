package com.tokopedia.discovery2.repository.supportingbrand

import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataResponse
import com.tokopedia.discovery2.data.gqlraw.GQL_COMPONENT
import com.tokopedia.discovery2.data.gqlraw.GQL_COMPONENT_QUERY_NAME
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SupportingBrandGQLRepository : BaseRepository(), SupportingBrandRepository {
    override suspend fun getData(
        componentId: String,
        pageIdentifier: String,
        filter: MutableMap<String, Any>
    ): Pair<List<ComponentsItem>, String?> {
        val response = getGQLData(
            GQL_COMPONENT,
            DataResponse::class.java,
            Utils.getComponentsGQLParams(
                componentId,
                pageIdentifier,
                Utils.getQueryString(filter)
            ),
            GQL_COMPONENT_QUERY_NAME
        ) as DataResponse

        val component = response.data.component

        val nextPage = component?.compAdditionalInfo?.nextPage

        if (component == null) return Pair(emptyList(), null)

        val list = withContext(Dispatchers.Default) {
            component.mapDataItemToSupportingBrandComponent()
        }

        return Pair(list, nextPage)
    }

    private fun ComponentsItem.mapDataItemToSupportingBrandComponent(): List<ComponentsItem> {

        val list = ArrayList<ComponentsItem>()

        data?.forEachIndexed { index, dataItem ->
            val componentsItem = ComponentsItem()
            componentsItem.apply {
                position = index
                name = ComponentNames.ShopOfferSupportingBrandItem.componentName
                properties = this@mapDataItemToSupportingBrandComponent.properties
                creativeName = this@mapDataItemToSupportingBrandComponent.creativeName
                compAdditionalInfo = this@mapDataItemToSupportingBrandComponent.compAdditionalInfo
                data = listOf(dataItem)

                list.add(componentsItem)
            }
        }

        return list
    }
}

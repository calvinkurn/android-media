package com.tokopedia.discovery2.repository.producthighlightrepository

import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataResponse
import com.tokopedia.discovery2.data.gqlraw.GQL_COMPONENT
import com.tokopedia.discovery2.data.gqlraw.GQL_COMPONENT_QUERY_NAME
import javax.inject.Inject

class ProductHighlightGQLRepository @Inject constructor() : BaseRepository(), ProductHighlightRepository {
    override suspend fun getProductHighlightData(componentId: String, queryParamterMap: MutableMap<String, Any>, pageEndPoint: String): ComponentsItem? {
        val response = (getGQLData(GQL_COMPONENT,
            DataResponse::class.java, Utils.getComponentsGQLParams(componentId, pageEndPoint, Utils.getQueryString(queryParamterMap)), GQL_COMPONENT_QUERY_NAME) as DataResponse)

        return response.data.component
    }
}

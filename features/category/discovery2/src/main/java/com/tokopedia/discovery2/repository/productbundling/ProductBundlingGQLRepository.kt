package com.tokopedia.discovery2.repository.productbundling

import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.data.DataResponse
import com.tokopedia.discovery2.data.gqlraw.GQL_COMPONENT
import com.tokopedia.discovery2.data.gqlraw.GQL_COMPONENT_QUERY_NAME
import javax.inject.Inject

class ProductBundlingGQLRepository @Inject constructor() : BaseRepository(), ProductBundlingRepository {
    override suspend fun getProductBundlingData(
            componentId: String,
            queryParamterMap: MutableMap<String, Any>,
            pageEndPoint: String,
            productBundlingComponentName: String?
    ): List<DataItem>? {
        val response = (getGQLData(
                GQL_COMPONENT,
                DataResponse::class.java, Utils.getComponentsGQLParams(componentId, pageEndPoint, Utils.getQueryString(queryParamterMap)), GQL_COMPONENT_QUERY_NAME
        ) as DataResponse)

        return response.data.component?.data
    }
}
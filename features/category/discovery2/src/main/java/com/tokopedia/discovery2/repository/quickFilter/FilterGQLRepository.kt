package com.tokopedia.discovery2.repository.quickFilter

import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.data.DataResponse
import com.tokopedia.discovery2.data.gqlraw.GQL_COMPONENT
import com.tokopedia.discovery2.data.gqlraw.GQL_COMPONENT_QUERY_NAME
import com.tokopedia.discovery2.discoverymapper.DiscoveryDataMapper
import com.tokopedia.filter.common.data.DynamicFilterModel
import javax.inject.Inject

class FilterGQLRepository @Inject constructor() : BaseRepository(), FilterRepository {

    override suspend fun getFilterData(componentId: String, queryParamterMap: MutableMap<String, Any>, pageEndPoint: String): DynamicFilterModel? {
        val response = (getGQLData(GQL_COMPONENT,
                DataResponse::class.java, Utils.getComponentsGQLParams(componentId, pageEndPoint, Utils.getQueryString(queryParamterMap)), GQL_COMPONENT_QUERY_NAME) as DataResponse)
        return DiscoveryDataMapper().mapFiltersToDynamicFilterModel(response.data.component?.data?.get(0))
    }
}
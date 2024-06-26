package com.tokopedia.discovery2.repository.tabs

import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.discovery2.Utils.Companion.getQueryMap
import com.tokopedia.discovery2.data.DataResponse
import com.tokopedia.discovery2.data.DiscoveryResponse
import com.tokopedia.discovery2.data.gqlraw.GQL_COMPONENT
import com.tokopedia.discovery2.data.gqlraw.GQL_COMPONENT_QUERY_NAME

open class TabsGQLRepository : BaseRepository(), TabsRepository {
    override suspend fun getDynamicTabData(componentId: String, pageIdentifier: String): DiscoveryResponse {
        return (getGQLData(GQL_COMPONENT,
                DataResponse::class.java, getQueryMap(componentId = componentId,
                pageIdentifier = pageIdentifier), GQL_COMPONENT_QUERY_NAME) as DataResponse).data
    }
}

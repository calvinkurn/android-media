package com.tokopedia.discovery2.repository.tabs

import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.discovery2.Utils.Companion.getQueryMap
import com.tokopedia.discovery2.data.DataResponse
import com.tokopedia.discovery2.data.DiscoveryResponse
import com.tokopedia.discovery2.data.gqlraw.GQL_COMPONENT

open class TabsGQLRepository : BaseRepository(), TabsRepository {
    override suspend fun getDynamicTabData(componentId: String, pageIdentifier: String, rpcDiscoquery: Map<String, String?>?): DiscoveryResponse {
        return (getGQLData(GQL_COMPONENT,
                DataResponse::class.java, getQueryMap(componentId, pageIdentifier, rpcDiscoquery), "componentInfo") as DataResponse).data
    }
}
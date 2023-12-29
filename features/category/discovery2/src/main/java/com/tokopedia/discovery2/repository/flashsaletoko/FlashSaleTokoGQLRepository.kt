package com.tokopedia.discovery2.repository.flashsaletoko

import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.discovery2.Utils
import com.tokopedia.discovery2.data.DataResponse
import com.tokopedia.discovery2.data.DiscoveryResponse
import com.tokopedia.discovery2.data.gqlraw.GQL_COMPONENT
import com.tokopedia.discovery2.data.gqlraw.GQL_COMPONENT_QUERY_NAME

class FlashSaleTokoGQLRepository : BaseRepository(), FlashSaleTokoRepository {
    override suspend fun getTabData(
        componentId: String,
        pageIdentifier: String
    ): DiscoveryResponse {
        val queryMap = Utils.getQueryMap(
            componentId = componentId,
            pageIdentifier = pageIdentifier
        )

        val response = getGQLData(
            GQL_COMPONENT,
            DataResponse::class.java,
            queryMap,
            GQL_COMPONENT_QUERY_NAME
        )

        return (response as DataResponse).data
    }
}

package com.tokopedia.discovery2.repository.tabs

import android.util.Log
import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.discovery2.data.DataResponse
import com.tokopedia.discovery2.data.DiscoveryResponse
import com.tokopedia.discovery2.data.gqlraw.GQL_COMPONENT

private const val IDENTIFIER = "identifier"
private const val COMPONENT_ID = "component_id"
private const val DEVICE = "device"
private const val DEVICE_VALUE = "Android"


open class TabsGQLRepository : BaseRepository(), TabsRepository {
    override suspend fun getDynamicTabData(componentId: String, pageIdentifier: String): DiscoveryResponse {
        return (getGQLData(GQL_COMPONENT,
                DataResponse::class.java, getQueryMap(componentId, pageIdentifier), "componentInfo") as DataResponse).data
    }


    private fun getQueryMap(componentId: String, pageIdentifier: String): Map<String, Any> {
        return mapOf(IDENTIFIER to pageIdentifier,
                DEVICE to DEVICE_VALUE,
                COMPONENT_ID to componentId
        )
    }
}
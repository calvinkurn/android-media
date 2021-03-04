package com.tokopedia.discovery2.repository.tabs

import com.tokopedia.discovery2.data.DiscoveryResponse

interface TabsRepository {
    suspend fun getDynamicTabData(componentId: String, pageIdentifier: String): DiscoveryResponse
}
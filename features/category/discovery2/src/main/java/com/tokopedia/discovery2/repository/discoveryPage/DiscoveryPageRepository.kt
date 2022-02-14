package com.tokopedia.discovery2.repository.discoveryPage

import com.tokopedia.discovery2.data.DiscoveryResponse

interface DiscoveryPageRepository {
    suspend fun getDiscoveryPageData(
        pageIdentifier: String,
        extraParams: Map<String, Any>? = null
    ): DiscoveryResponse
}
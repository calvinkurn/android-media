package com.tokopedia.discovery2.repository.discoveryPage

import com.tokopedia.discovery2.data.DiscoveryResponse

interface DiscoveryPageRepository {
    companion object  {
        lateinit var discoveryResponseData: DiscoveryResponse
    }
    suspend fun getDiscoveryPageData(pageIdentifier: String) : DiscoveryResponse
}
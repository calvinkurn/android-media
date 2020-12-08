package com.tokopedia.discovery2.repository.quickFilter

import com.tokopedia.discovery2.data.DiscoveryResponse

interface IQuickFilterGqlRepository {
    suspend fun getQuickFilterProductCountData(componentId: String, pageEndPoint: String, mapParams: Map<String, String>, userId: String?): DiscoveryResponse
}
package com.tokopedia.discovery2.repository.discoveryPage

import com.tokopedia.basemvvm.repository.BaseRepository
import com.tokopedia.discovery2.data.DiscoveryPageUIConfigData
import javax.inject.Inject

class DiscoveryUIConfigGQLRepository @Inject constructor(val query: String)
    : BaseRepository() {

    suspend fun getDiscoveryUIConfigData(): DiscoveryPageUIConfigData {
        return getGQLData(query,
                DiscoveryPageUIConfigData::class.java, mapOf()) as DiscoveryPageUIConfigData
    }
}
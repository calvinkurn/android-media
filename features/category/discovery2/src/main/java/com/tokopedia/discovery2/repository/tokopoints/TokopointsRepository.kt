package com.tokopedia.discovery2.repository.tokopoints

import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DiscoveryResponse

interface TokopointsRepository {
    suspend fun getTokopointsData(componentId: Int, queryParamterMap : MutableMap<String, Any>): ArrayList<ComponentsItem>
}
package com.tokopedia.discovery2.repository.tokopoints

import com.tokopedia.discovery2.data.ComponentsItem

interface TokopointsRepository {
    suspend fun getTokopointsData(componentId: Int, queryParamterMap: MutableMap<String, Any>, pageEndPoint: String): ArrayList<ComponentsItem>
}
package com.tokopedia.discovery2.repository.producthighlightrepository

import com.tokopedia.discovery2.data.ComponentsItem

interface ProductHighlightRepository {
    suspend fun getProductHighlightData(componentId: String, queryParamterMap: MutableMap<String, Any>, pageEndPoint: String): ComponentsItem?
}

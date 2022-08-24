package com.tokopedia.discovery2.repository.productbundling

import com.tokopedia.discovery2.data.ComponentsItem

interface ProductBundlingRepository {
    suspend fun getProductBundlingData(componentId: String, queryParamterMap: MutableMap<String, Any>, pageEndPoint: String, productBundlingComponentName: String?): ArrayList<ComponentsItem>
}
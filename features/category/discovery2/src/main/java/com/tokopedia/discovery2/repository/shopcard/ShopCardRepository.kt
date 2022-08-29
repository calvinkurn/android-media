package com.tokopedia.discovery2.repository.shopcard

import com.tokopedia.discovery2.data.ComponentsItem

interface ShopCardRepository {
    suspend fun getShopCardData(componentId: String, queryParamterMap: MutableMap<String, Any>, pageEndPoint: String, shopCardComponentName: String?): Pair<ArrayList<ComponentsItem>,String?>
}
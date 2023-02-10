package com.tokopedia.discovery2.repository.productcards

import com.tokopedia.discovery2.data.ComponentAdditionalInfo
import com.tokopedia.discovery2.data.ComponentsItem

interface ProductCardsRepository {
    suspend fun getProducts(componentId: String, queryParamterMap: MutableMap<String, Any>, pageEndPoint: String, productComponentName: String?): Pair<ArrayList<ComponentsItem>, ComponentAdditionalInfo?>
}

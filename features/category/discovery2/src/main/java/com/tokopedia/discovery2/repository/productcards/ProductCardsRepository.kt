package com.tokopedia.discovery2.repository.productcards

import com.tokopedia.discovery2.data.ComponentsItem

interface  ProductCardsRepository {
    suspend fun getProducts(componentId: Int, queryParamterMap: MutableMap<String, Any>, pageEndPoint: String, isHorizontal : Boolean = false): ArrayList<ComponentsItem>
}
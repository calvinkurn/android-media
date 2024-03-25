package com.tokopedia.discovery2.repository.productcards

import com.tokopedia.discovery2.data.ComponentAdditionalInfo
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.productcarditem.ProductCardRequest

interface ProductCardsRepository {
    suspend fun getProducts(
        requestParams: ProductCardRequest,
        queryParameterMap: MutableMap<String, Any>
    ): Pair<ArrayList<ComponentsItem>, ComponentAdditionalInfo?>
}

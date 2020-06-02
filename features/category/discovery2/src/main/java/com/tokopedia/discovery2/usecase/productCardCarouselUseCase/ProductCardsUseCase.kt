package com.tokopedia.discovery2.usecase.productCardCarouselUseCase

import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.repository.productcards.ProductCardsRepository
import javax.inject.Inject

class ProductCardsUseCase @Inject constructor(private val productCardsRepository: ProductCardsRepository) {

    suspend fun getProductCardsUseCase(componentId: Int, queryParameterMap: MutableMap<String, Any>, pageEndPoint: String, productComponentName: String?): ArrayList<ComponentsItem> {
        return productCardsRepository.getProducts(componentId, queryParameterMap, pageEndPoint, productComponentName)
    }
}
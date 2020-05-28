package com.tokopedia.discovery2.usecase.productCardCarouselUseCase

import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.repository.productcards.ProductCardsRepository
import javax.inject.Inject

class ProductCardCarouselUseCase @Inject constructor(private val productCardsRepository: ProductCardsRepository) {

    suspend fun getProductCardCarouselUseCase(componentId: Int, queryParameterMap: MutableMap<String, Any>, pageEndPoint: String, isHorizontal : Boolean = false): ArrayList<ComponentsItem> {
        return productCardsRepository.getProducts(componentId, queryParameterMap, pageEndPoint, isHorizontal)
    }
}
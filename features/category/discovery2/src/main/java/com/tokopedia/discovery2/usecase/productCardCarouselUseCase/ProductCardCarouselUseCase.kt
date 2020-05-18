package com.tokopedia.discovery2.usecase.productCardCarouselUseCase

import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.repository.productcards.ProductCardsRepository
import com.tokopedia.discovery2.repository.tokopoints.TokopointsRepository
import javax.inject.Inject

class ProductCardCarouselUseCase @Inject constructor(val productCardsRepository: ProductCardsRepository) {

    suspend fun getProductCardCarouselUseCase(componentId: Int, queryParamterMap: MutableMap<String, Any>, pageEndPoint: String): ArrayList<ComponentsItem> {
        return productCardsRepository.getProducts(componentId, queryParamterMap, pageEndPoint)
    }
}
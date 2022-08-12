package com.tokopedia.discovery2.usecase.productbundlingusecase

import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.repository.productbundling.ProductBundlingRepository
import javax.inject.Inject

class ProductBundlingUseCase @Inject constructor(private val productBundlingRepository: ProductBundlingRepository) {

    suspend fun loadFirstPageComponents(componentId: String, pageEndPoint: String): Boolean {
        val component = getComponent(componentId, pageEndPoint)
        if (component?.noOfPagesLoaded == 1) return false
        component?.let {
            val isDynamic = it.properties?.dynamic ?: false
            val productListData = productBundlingRepository.getProductBundlingData(
                    if (isDynamic && !component.dynamicOriginalId.isNullOrEmpty())
                        component.dynamicOriginalId!! else componentId,
                    mutableMapOf(),
                    pageEndPoint, it.name)
            val bundleProductListData = (productListData?.data ?: emptyList()).toMutableList()
            if (bundleProductListData.isEmpty()) return true
            return true
        }
        return false
    }
}
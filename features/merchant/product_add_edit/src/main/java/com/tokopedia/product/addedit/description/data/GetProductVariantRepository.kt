package com.tokopedia.product.addedit.description.data

import com.tokopedia.product.addedit.description.data.remote.ProductVariantService
import com.tokopedia.product.addedit.description.data.remote.model.variantbycat.ProductVariantByCatModel
import javax.inject.Inject

class GetProductVariantRepository @Inject constructor(
        private val service: ProductVariantService
) {
    suspend fun getVariant(categoryId: String): List<ProductVariantByCatModel> {
        val response = service.getVariant(categoryId)
        return response.data
    }
}
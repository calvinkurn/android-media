package com.tokopedia.catalog.domain

import com.tokopedia.oldcatalog.model.raw.CatalogComparisonProductsResponse
import com.tokopedia.oldcatalog.repository.CatalogComparisonProductRepository
import javax.inject.Inject

class CatalogComparisonProductUseCase @Inject constructor(private val catalogComparisonProductRepository: CatalogComparisonProductRepository) {

    suspend fun getCatalogComparisonProducts(
        catalogId: String,
        brand: String,
        categoryId: String,
        limit: String,
        page: String,
        name: String
    ): CatalogComparisonProductsResponse {
        val gqlResponse = catalogComparisonProductRepository.getComparisonProducts(
            catalogId,
            brand,
            categoryId,
            limit,
            page,
            name
        )

        val error = gqlResponse?.getError(CatalogComparisonProductsResponse::class.java)

        return if (error.isNullOrEmpty()) {
            gqlResponse?.getData(
                CatalogComparisonProductsResponse::class.java
            ) ?: throw Throwable("No catalog product data found")
        } else {
            throw Throwable("No catalog product data found")
        }
    }
}

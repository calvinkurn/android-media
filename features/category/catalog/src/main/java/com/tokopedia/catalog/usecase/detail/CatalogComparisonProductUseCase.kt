package com.tokopedia.catalog.usecase.detail

import com.tokopedia.catalog.model.raw.CatalogComparisonProductsResponse
import com.tokopedia.catalog.model.raw.CatalogProductReviewResponse
import com.tokopedia.catalog.repository.CatalogComparisonProductRepository
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class CatalogComparisonProductUseCase @Inject constructor(private val catalogComparisonProductRepository: CatalogComparisonProductRepository) {

    suspend fun getCatalogComparisonProducts(catalogId: String, brand : String, categoryId : String,
                                             limit: String, page : String, name : String) : Result<CatalogComparisonProductsResponse>  {
        val gqlResponse = catalogComparisonProductRepository.getComparisonProducts(catalogId,brand,
            categoryId,limit,page,name)
        val data = gqlResponse?.getData<CatalogComparisonProductsResponse>(
            CatalogComparisonProductsResponse::class.java)
        return if(data != null)
            Success(data)
        else{
            Fail(Throwable("No catalog product data found"))
        }
    }
}
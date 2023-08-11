package com.tokopedia.oldcatalog.usecase.detail

import com.tokopedia.oldcatalog.model.raw.CatalogProductReviewResponse
import com.tokopedia.oldcatalog.repository.CatalogAllReviewRepository
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class CatalogAllReviewUseCase @Inject constructor(private val catalogAllReviewsRepository: CatalogAllReviewRepository) {

    suspend fun getCatalogReviews(catalogId : String , key : String, value : String) : Result<CatalogProductReviewResponse>  {
        val gqlResponse = catalogAllReviewsRepository.getAllReviews(catalogId, key, value)
        val data = gqlResponse?.getData<CatalogProductReviewResponse>(CatalogProductReviewResponse::class.java)
        return if(data != null)
            Success(data)
        else{
            Fail(Throwable("No data found"))
        }
    }
}

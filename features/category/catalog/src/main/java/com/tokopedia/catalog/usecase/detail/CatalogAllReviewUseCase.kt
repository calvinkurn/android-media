package com.tokopedia.catalog.usecase.detail

import androidx.lifecycle.MutableLiveData
import com.tokopedia.catalog.model.raw.CatalogProductReviewResponse
import com.tokopedia.catalog.repository.CatalogAllReviewRepository
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class CatalogAllReviewUseCase @Inject constructor(private val catalogAllReviewsRepository: CatalogAllReviewRepository) {

    suspend fun getCatalogReviews(catalogId : String , key : String, value : String,
                                 catalogDetailDataModel: MutableLiveData<Result<CatalogProductReviewResponse>>)  {
        val gqlResponse = catalogAllReviewsRepository.getAllReviews(catalogId, key, value)
        val data = gqlResponse?.getData<CatalogProductReviewResponse>(CatalogProductReviewResponse::class.java)
        if(data != null)
            catalogDetailDataModel.value = Success(data)
        else{
            catalogDetailDataModel.value = Fail(Throwable("No data found"))
        }
    }
}
package com.tokopedia.catalog.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.catalog.model.raw.CatalogProductReviewResponse
import com.tokopedia.catalog.usecase.detail.CatalogAllReviewUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import javax.inject.Inject

class CatalogAllReviewsViewModel @Inject constructor(private var catalogAllReviewUseCase: CatalogAllReviewUseCase) : BaseViewModel() {

    private val catalogAllReviewsModel = MutableLiveData<Result<CatalogProductReviewResponse>>()

    fun getAllReviews(catalogId: String, key : String, value : String) {
        viewModelScope.launchCatchError(
                block = {
                    catalogAllReviewsModel.value = catalogAllReviewUseCase.getCatalogReviews(catalogId,key,value)
                },
                onError = {
                    catalogAllReviewsModel.value = Fail(it)
                }
        )
    }

    fun getCatalogAllReviewsModel(): MutableLiveData<Result<CatalogProductReviewResponse>> {
        return catalogAllReviewsModel
    }
}
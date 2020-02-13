package com.tokopedia.brandlist.brandlist_search.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.brandlist.brandlist_search.data.model.BrandListSearchRecommendationResponse
import com.tokopedia.brandlist.brandlist_search.domain.SearchRecommedationBrandUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BrandlistSearchRecommendationViewModel @Inject constructor(
        private val searchRecommendedBrandUseCase: SearchRecommedationBrandUseCase,
        dispatcher: CoroutineDispatcher
): BaseViewModel(dispatcher){

    val _brandlistSearchRecommendationResponse = MutableLiveData<Result<BrandListSearchRecommendationResponse>>()
    val brandlistSearchRecommendationResponse: LiveData<Result<BrandListSearchRecommendationResponse>>
        get() =_brandlistSearchRecommendationResponse

    fun searchRecommendation(
            userId: Int?,
            categoryIds: String
    ) {
        searchRecommendedBrandUseCase.cancelJobs()
        launchCatchError(block = {
            withContext(Dispatchers.IO) {
                searchRecommendedBrandUseCase.params = SearchRecommedationBrandUseCase.
                        createRequestParam(userId, categoryIds)
                val searchRecommendationResult = searchRecommendedBrandUseCase.executeOnBackground()
                searchRecommendationResult.let {
                    _brandlistSearchRecommendationResponse.postValue(Success(it))
                }
            }
        }) {
            _brandlistSearchRecommendationResponse.value = Fail(it)
        }
    }

}
package com.tokopedia.brandlist.brandlist_search.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.brandlist.brandlist_search.data.model.BrandlistSearchResponse
import com.tokopedia.brandlist.brandlist_search.domain.SearchBrandUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.coroutines.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class BrandlistSearchViewModel @Inject constructor(
        private val searchBrandUseCase: SearchBrandUseCase,
        dispatcher: CoroutineDispatcher
): BaseViewModel(dispatcher) {

    val brandlistSearchResponse = MutableLiveData<Result<BrandlistSearchResponse>>()

    fun searchBrand(
            categoryId: Int,
            offset: Int,
            query: String,
            brandSize: Int,
            sortType: Int,
            firstLetter: String
    ) {
        searchBrandUseCase.cancelJobs()
        launchCatchError(block = {
            coroutineScope {
                launch(Dispatchers.IO) {
                    val searchBrandResult = getSearchBrandResponse(
                            categoryId,
                            offset,
                            query,
                            brandSize,
                            sortType,
                            firstLetter
                    )
                    searchBrandResult.let {
                        brandlistSearchResponse.postValue(Success(it))
                    }
                }
            }
        }) {
            brandlistSearchResponse.value = Fail(it)
        }
    }

    private suspend fun getSearchBrandResponse(
            categoryId: Int,
            offset: Int,
            query: String,
            brandSize: Int,
            sortType: Int,
            firstLetter: String
    ): BrandlistSearchResponse {
        var brandSearchResult = BrandlistSearchResponse()
        try {
            searchBrandUseCase.params = SearchBrandUseCase.createRequestParam(
                    categoryId,
                    offset,
                    query,
                    brandSize,
                    sortType,
                    firstLetter
            )
            brandSearchResult = searchBrandUseCase.executeOnBackground()
        } catch (t: Throwable) {
        }
        return brandSearchResult
    }

}
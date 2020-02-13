package com.tokopedia.brandlist.brandlist_search.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.brandlist.brandlist_search.data.model.BrandlistSearchResponse
import com.tokopedia.brandlist.brandlist_search.domain.SearchBrandUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.coroutines.Result
import kotlinx.coroutines.*
import javax.inject.Inject

class BrandlistSearchViewModel @Inject constructor(
        private val searchBrandUseCase: SearchBrandUseCase,
        dispatcher: CoroutineDispatcher
): BaseViewModel(dispatcher) {

    val _brandlistSearchResponse = MutableLiveData<Result<BrandlistSearchResponse>>()
    val brandlistSearchResponse: LiveData<Result<BrandlistSearchResponse>>
        get() = _brandlistSearchResponse


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
            withContext(Dispatchers.IO) {
                searchBrandUseCase.params = SearchBrandUseCase.createRequestParam(categoryId, offset,
                        query, brandSize, sortType, firstLetter)
                val searchBrandResult = searchBrandUseCase.executeOnBackground()
                searchBrandResult.let {
                    _brandlistSearchResponse.postValue(Success(it))
                }
            }
        }) {
            _brandlistSearchResponse.value = Fail(it)
        }
    }

}
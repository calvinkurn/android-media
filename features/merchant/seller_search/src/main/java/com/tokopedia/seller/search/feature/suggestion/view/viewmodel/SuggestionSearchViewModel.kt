package com.tokopedia.seller.search.feature.suggestion.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.seller.search.common.domain.GetSellerSearchUseCase
import com.tokopedia.seller.search.common.domain.mapper.GlobalSearchSellerMapper
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.seller.search.feature.suggestion.domain.usecase.InsertSuccessSearchUseCase
import com.tokopedia.seller.search.feature.suggestion.view.model.BaseSuggestionSearchSeller
import com.tokopedia.seller.search.feature.suggestion.view.model.registersearch.RegisterSearchUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SuggestionSearchViewModel @Inject constructor(
    private val dispatcherProvider: CoroutineDispatchers,
    private val getSellerSearchUseCase: GetSellerSearchUseCase,
    private val insertSellerSearchUseCase: InsertSuccessSearchUseCase
) : BaseViewModel(dispatcherProvider.main) {

    private val _getSearchSeller = MutableLiveData<Result<List<BaseSuggestionSearchSeller>>>()
    val getSellerSearch: LiveData<Result<List<BaseSuggestionSearchSeller>>>
        get() = _getSearchSeller

    private val _insertSuccessSearch = MutableLiveData<Result<RegisterSearchUiModel>>()
    val insertSuccessSearch: LiveData<Result<RegisterSearchUiModel>>
        get() = _insertSuccessSearch

    fun getSellerSearch(keyword: String, section: String = "", shopId: String) {
        launchCatchError(block = {
            val responseGetSellerSearch  = withContext(dispatcherProvider.io) {
                getSellerSearchUseCase.params = GetSellerSearchUseCase.createParams(keyword, shopId, section)
                GlobalSearchSellerMapper.mapToSellerSearchVisitable(getSellerSearchUseCase.executeOnBackground(), keyword)
            }
            _getSearchSeller.postValue(Success(responseGetSellerSearch))
        }, onError = {
            _getSearchSeller.postValue(Fail(it))
        })
    }

    fun insertSearchSeller(keyword: String, id: String, title: String, index: Int) {
        launchCatchError(block = {
            val responseInsertSearch = withContext(dispatcherProvider.io) {
                insertSellerSearchUseCase.params = InsertSuccessSearchUseCase.createParams(
                        keyword, id, title, index)
                GlobalSearchSellerMapper.mapToRegisterSearchUiModel(insertSellerSearchUseCase.executeOnBackground())
            }
            _insertSuccessSearch.postValue(Success(responseInsertSearch))
        }, onError = {
            _insertSuccessSearch.postValue(Fail(it))
        })
    }
}

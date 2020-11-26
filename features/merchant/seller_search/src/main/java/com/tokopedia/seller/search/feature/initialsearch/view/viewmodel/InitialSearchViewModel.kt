package com.tokopedia.seller.search.feature.initialsearch.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.seller.search.common.domain.GetSellerSearchUseCase
import com.tokopedia.seller.search.common.domain.mapper.GlobalSearchSellerMapper
import com.tokopedia.seller.search.common.util.CoroutineDispatcherProvider
import com.tokopedia.seller.search.feature.initialsearch.domain.usecase.DeleteSuggestionHistoryUseCase
import com.tokopedia.seller.search.feature.initialsearch.view.model.deletehistory.DeleteHistorySearchUiModel
import com.tokopedia.seller.search.feature.initialsearch.view.model.initialsearch.InitialSearchUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import javax.inject.Inject

class InitialSearchViewModel @Inject constructor(
        private val dispatcherProvider: CoroutineDispatcherProvider,
        private val getSellerSearchUseCase: GetSellerSearchUseCase,
        private val deleteSuggestionHistoryUseCase: DeleteSuggestionHistoryUseCase
) : BaseViewModel(dispatcherProvider.main()) {

    private val _deleteHistorySearch = MutableLiveData<Result<DeleteHistorySearchUiModel>>()
    val deleteHistorySearch: LiveData<Result<DeleteHistorySearchUiModel>>
        get() = _deleteHistorySearch

    private val _getSearchSeller = MutableLiveData<Result<InitialSearchUiModel>>()
    val getSellerSearch: LiveData<Result<InitialSearchUiModel>>
        get() = _getSearchSeller

    fun getSellerSearch(keyword: String, section: String = "", shopId: String) {
        launchCatchError(block = {
            val responseGetSellerSearch = withContext(dispatcherProvider.io()) {
                getSellerSearchUseCase.params = GetSellerSearchUseCase.createParams(
                        keyword, shopId, section)
                GlobalSearchSellerMapper.mapToInitialSearchUiModel(getSellerSearchUseCase.executeOnBackground())
            }
            _getSearchSeller.postValue(Success(responseGetSellerSearch))
        }, onError = {
            _getSearchSeller.postValue(Fail(it))
        })
    }

    fun deleteSuggestionSearch(keyword: List<String>) {
        launchCatchError(block = {
            val responseDeleteSearch = withContext(dispatcherProvider.io()) {
                deleteSuggestionHistoryUseCase.params = DeleteSuggestionHistoryUseCase.createParams(keyword)
                GlobalSearchSellerMapper.mapToDeleteHistorySearchUiModel(deleteSuggestionHistoryUseCase.executeOnBackground())
            }
            _deleteHistorySearch.postValue(Success(responseDeleteSearch))
        }, onError = {
            _deleteHistorySearch.postValue(Fail(it))
        })
    }

}

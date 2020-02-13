package com.tokopedia.topchat.chatsearch.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.topchat.chatsearch.data.GetChatSearchResponse
import com.tokopedia.topchat.chatsearch.data.SearchResult
import com.tokopedia.topchat.chatsearch.usecase.GetSearchQueryUseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class ChatSearchViewModel @Inject constructor(
        dispatcher: CoroutineDispatcher,
        private val getSearchQueryUseCase: GetSearchQueryUseCase
) : BaseViewModel(dispatcher) {

    val hasNext: Boolean get() = getSearchQueryUseCase.hasNext

    private var _loadInitialData = MutableLiveData<Boolean>()
    val loadInitialData: LiveData<Boolean> get() = _loadInitialData

    private var _emptyQuery = MutableLiveData<Boolean>()
    val emptyQuery: LiveData<Boolean> get() = _emptyQuery

    private var _triggerSearch = MutableLiveData<String>()
    val triggerSearch: LiveData<String> get() = _triggerSearch

    private var _errorMessage = MutableLiveData<Throwable>()
    val errorMessage: LiveData<Throwable> get() = _errorMessage

    private var _searchResults = MutableLiveData<List<SearchResult>>()
    val searchResult: LiveData<List<SearchResult>> get() = _searchResults

    var query: String = ""
    var page: Int = 1

    private var canRetry = false

    fun onSearchQueryChanged(newQuery: String) {
        if (newQuery == query) return
        query = newQuery
        page = 1
        if (query.isEmpty()) {
            getSearchQueryUseCase.cancelRunningSearch()
            _emptyQuery.postValue(true)
            return
        }
        if (getSearchQueryUseCase.isSearching) getSearchQueryUseCase.cancelRunningSearch()
        _loadInitialData.postValue(true)
        doSearch()
    }

    fun loadPage(page: Int) {
        if (page > this.page && getSearchQueryUseCase.hasNext) {
            loadNextPage(page)
        } else if (page == this.page && canRetry) {
            retryLoadCurrentPage()
        }
    }

    private fun loadNextPage(nextPage: Int) {
        this.page = nextPage
        doSearch()
    }

    private fun retryLoadCurrentPage() {
        doSearch()
    }

    fun isFirstPage(): Boolean {
        return page == 1
    }

    private fun doSearch() {
        _triggerSearch.postValue(query)
        getSearchQueryUseCase.doSearch(::onSuccessDoSearch, ::onErrorDoSearch, query, page)
    }

    private fun onSuccessDoSearch(response: GetChatSearchResponse) {
        canRetry = false
        _searchResults.postValue(response.searchResults)
    }

    private fun onErrorDoSearch(throwable: Throwable) {
        canRetry = true
        _errorMessage.postValue(throwable)
    }
}
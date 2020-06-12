package com.tokopedia.topchat.chatsearch.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.topchat.chatsearch.data.GetChatSearchResponse
import com.tokopedia.topchat.chatsearch.data.SearchResult
import com.tokopedia.topchat.chatsearch.usecase.GetSearchQueryUseCase
import com.tokopedia.topchat.chatsearch.view.uimodel.ContactLoadMoreUiModel
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

    private var _searchResults = MutableLiveData<List<Visitable<*>>>()
    val searchResult: LiveData<List<Visitable<*>>> get() = _searchResults

    var firstContactSearchResults: GetChatSearchResponse = GetChatSearchResponse()

    var query: String = ""
    private var page: Int = 1
    private var canRetry = false

    fun onSearchQueryChanged(newQuery: String) {
        if (newQuery == query) return
        query = newQuery
        page = 1
        if (query.isEmpty()) {
            getSearchQueryUseCase.cancelRunningSearch()
            _emptyQuery.value = true
            return
        }
        if (getSearchQueryUseCase.isSearching) getSearchQueryUseCase.cancelRunningSearch()
        _loadInitialData.value = true
        doSearch()
    }

    fun loadPage(page: Int) {
        if (page > this.page && getSearchQueryUseCase.hasNext) {
            loadNextPage(page)
        } else if (page == this.page && canRetry) {
            retryLoadCurrentPage()
        }
    }

    fun disableEmptyQuery() {
        _emptyQuery.value = false
    }

    fun isFirstPage(): Boolean {
        return page == 1
    }

    private fun loadNextPage(nextPage: Int) {
        this.page = nextPage
        doSearch()
    }

    private fun retryLoadCurrentPage() {
        doSearch()
    }

    private fun doSearch() {
        _triggerSearch.value = query
        getSearchQueryUseCase.doSearch(::onSuccessDoSearch, ::onErrorDoSearch, query, page)
    }

    private fun onSuccessDoSearch(response: GetChatSearchResponse, contactLoadMore: ContactLoadMoreUiModel?) {
        canRetry = false
        if (isFirstPage()) {
            firstContactSearchResults = response
        }
        if (isFirstPage() && contactLoadMore != null) {
            val searchResults: MutableList<Visitable<*>> = response.searchResults.toMutableList()
            searchResults.add(0, contactLoadMore)
            _searchResults.value = searchResults
        } else {
            _searchResults.value = response.searchResults
        }
    }

    private fun onErrorDoSearch(throwable: Throwable) {
        canRetry = true
        _errorMessage.value = throwable
    }
}
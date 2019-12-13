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

    var loadInitialData = MutableLiveData<Boolean>()
    var emptyQuery = MutableLiveData<Boolean>()
    var errorMessage = MutableLiveData<Throwable>()

    private var _searchResults = MutableLiveData<List<SearchResult>>()
    val searchResult: LiveData<List<SearchResult>>
        get() = _searchResults

    var query: String = ""
    var page: Int = 1

    private var canRetry = false

    fun onSearchQueryChanged(newQuery: String) {
        if (newQuery == query) return
        query = newQuery
        page = 1
        if (query.isEmpty()) {
            getSearchQueryUseCase.cancelRunningSearch()
            emptyQuery.postValue(true)
            return
        }
        if (getSearchQueryUseCase.isSearching) getSearchQueryUseCase.cancelRunningSearch()
        loadInitialData.postValue(true)
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
        getSearchQueryUseCase.doSearch(::onSuccessDoSearch, ::onErrorDoSearch, query, page)
    }

    private fun onSuccessDoSearch(response: GetChatSearchResponse) {
        canRetry = false
        _searchResults.postValue(response.searchResults)
    }

    private fun onErrorDoSearch(throwable: Throwable) {
        canRetry = true
        errorMessage.postValue(throwable)
    }
}